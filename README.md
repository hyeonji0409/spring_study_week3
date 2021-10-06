# spring_study_week3

1. 데이터베이스 사용법(JPA)
2. 예외처리

## 데이터 영속화하기
+ ORM(Object Relation Mapping)이란?
  + Database에 있는 Relation과 java에 있는 객체들을 Mapping한다는 의미 
  + Native Query를 작성하지 않고도 영속화가 가능
  + 벤더에 독립적이게 영속화 가능

+ H2 Database
  + 메모리 데이터베이스로 예제 프로젝트에서 주로 사용
  + 콘솔 설정을 하면 웹 기반 DBMS 화면 볼 수 있음

+ JPA, H2 의존성 추가
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'com.h2database:h2'
```

+ JPA의 관리를 위해 Article 도메인을 엔티티로 변경
```java
//domain/Article.java

@Getter
@Builder
@NoArgsConstructor //파라미터가 없는 기본 생성자를 만들어달라
@AllArgsConstructor //없으면 Builder에 오류가 남
@Entity
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String title;
  String content;
}
```

+ ArticleRepository 생성
  + Domain 관리를 위함 => Query 수행 가능
```java
public interface ArticleRepository extends CrudRepository<Article, Long> { } // <도메인, 도메인 타입>
```

+ ArticleService가 ArticleRepository 객체와 연계될 수 있도록 설정
```java
// service/ArticleService.java

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Long save(Article request) {
        return articleRepository.save(request).getId();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Article update(Article request) {
        Article article = this.findById(request.getId());
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    public void delete(Long id) {
        Article article = this.findById(id);
        articleRepository.deleteById(id); // = articleRepository.delete(article);
    }
}
```
+ Update 함수 코드는 Repository 클래스를 활용하지 않고 도메인 클래스에 바로 접근하도록 설정함
+ JPA는 Persistance Context라는 논리적 공간에서 엔티티를 캐싱하고 관리함
+ JPA는 하나의 트랜잭션이 끝나면 수정된 domain의 내용을 자동으로 확인하여 실제 데이터베이스 벤더에 저장

```java
// domain/Article.java

public void update(String title, String content) {
        this.title = title;
        this.content  = content;
    }
```

## Trasaction
+ 원자성이 보장되어야 하는 업무 단위
+ update = 조회와 수정의 두 역할을 모두 수행

## CRUD API 만들기
+ ArticleController 객체에 PUT, DELETE API 추가하기
```java
//controller/ArticleController.java

@PutMapping("/{id}")
public Response<ArticleDto.Res> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        Article article = Article.builder()
        .id(id)
        .title(request.getTitle())
        .content(request.getContent())
        .build();

        Article articleResponse = articleService.update(article);

        ArticleDto.Res response = ArticleDto.Res.builder()
        .id(String.valueOf(articleResponse.getId()))
        .title(articleResponse.getTitle())
        .content(articleResponse.getContent())
        .build();

        return Response.<ArticleDto.Res>builder().code(ApiCode.SUCESS).data(response).build();
        }

@DeleteMapping("/{id}")
public Response<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return Response.<Void>builder().code(ApiCode.SUCESS).build();
        }
```

+ ArticleDto에 PutDto 생성
```java
@Getter
    public static class ReqPut{
        String title;
        String content;
    }
```

## H2 DATABASE
+ 메모리 DB
+ 사용하기 편리해서 예제코드에서 많이 사용
+ localhost:8080/h2-console에 접근하면 확인 가능
```
// application.properties

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
```

## of Pattern
+ 정적 팩토리 메서드 패턴
+ Controller에서 할 수 있는 일을 대신 해줌(응집도를 낮추기 위함 + 가독성)
+ 특정 객체를 생성하는 코드들이 Controller에 중복되는 것을 막기 위함
+ 객체를 생성하는 일을 그 객체에 위임함으로써 보다 객체지향과 같은 코딩을 할 수 있음

+ Article에 of 패턴 생성
```java
//domain/Article.java

public static Article of(ArticleDto.ReqPost from) {
        return Article.builder()
        .title(from.getTitle())
        .content(from.getContent())
        .build();
        }

public static Article of(ArticleDto.ReqPut from, Long id) {
        return Article.builder()
        .id(id)
        .title(from.getTitle())
        .content(from.getContent())
        .build();
        }
```

+ ArticleDto에 of 패턴 생성
```java
 public static Res of(Article from) {
        return Res.builder()
        .id(String.valueOf(from.getId()))
        .title(String.valueOf(from.getTitle()))
        .content(String.valueOf(from.getContent()))
        .build();
        }
```

+ Response에 of 패턴 생성
```java
// dto/Response.java

public static Response<Void> ok() { // 데이터를 무조건 넣게 되어있어서 생기는 오류를 해결하기 위함.
        return Response.<Void>builder()
        .code(ApiCode.SUCESS)
        .build();
        }
public static <T> Response<T> ok(T data) {
        return Response.<T>builder()
        .code(ApiCode.SUCESS)
        .data(data)
        .build();
        }
```

+ of 패턴을 사용하면서 Controller의 코드가 다음과 같이 간단해짐
  + BUT, 파라미터가 많아지는 경우에는 지양하는 편이다. (코드가 지저분한건 같기 때문)

```java
//controller/ArticleController.java

public class ArticleController {
  private final ArticleService articleService;

  @PostMapping
  public Response<Long> post(@RequestBody ArticleDto.ReqPost request) {
    return Response.ok(articleService.save(Article.of(request)));
  }

  @GetMapping("/{id}")
  public Response<ArticleDto.Res> get(@PathVariable Long id) {
    return Response.ok(ArticleDto.Res.of(articleService.findById(id)));
  }

  @PutMapping("/{id}")
  public Response<ArticleDto.Res> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
    return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
  }

  @DeleteMapping("/{id}")
  public Response<Void> delete(@PathVariable Long id) {
    return Response.ok();
  }
```

## 예외처리
+ 실무에서는 오류를 잡아내는 예외처리가 매우 중요
+ NullPointerException이 발생할 수 있는 영역을 찾고, null이 발생했을 때 예외를 발생시킨다면? => 실무에서 매우 위험!!!

+ 여러 오류들
  1. 함수가 어떤 타입을 받는지의 가독성의 효과를 얻을 수 있었으나, Object를 받음으로 원래 하려던 것이 깨짐.
  2. try-catch문을 넣으면서 코드가 장황해짐. (메인코드 이상의 부수적인 것들이 추가 생성됨)
  3. 데이터가 존재하지 않는 경우에만 API 코드를 넣었으나, 동일 함수에 data가 존재하지 않는 것 이외의 다른 오류가 발생되면 코드에 지정되어있는 것은 한정적이므로 유연성이 떨어짐
```java
//service/ArticleService.java

@PutMapping("/{id}")
public Response<Object> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        try {
        return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
        } catch (RuntimeException e){
        return Response.builder().code(ApiCode.DATA_IS_NOT_FOUND).data(e.getMessage()).build();
        }
        }

@DeleteMapping("/{id}")
public Response<Void> delete(@PathVariable Long id) {
        return Response.ok();
        }
```

+ 위의 단점 해결 위해 Exception 객체 생성
  + 유연성을 가진 예외처리를 해보자!!
```java
// exception/ApiException.java

@Getter
public class ApiException extends RuntimeException{
    private final ApiCode code;

    public ApiException(ApiCode code) {
        this.code = code;
    }

    public ApiException(ApiCode code, String msg) {
        super(msg);
        this.code = code;
    }
}
```
```java
// service/ArticleService.java
 @Transactional
    public Article update(Article request) {
        Article article = this.findById(request.getId());

        if(Objects.isNull(article)) {
            throw new ApiException(ApiCode.DATA_IS_NOT_FOUND, "Article value is not existed");
        }

        article.update(request.getTitle(), request.getContent());

        return article;
    }
```
+ 던지는 api 코드에 따라서 응답을 바꿀 수 있음
```java
// controller/ArticleController.java
 @PutMapping("/{id}")
    public Response<Object> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        try {
            return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
        } catch (ApiException e){
            return Response.builder().code(e.getCode()).data(e.getMessage()).build();
        }
    }
```

+ try-catch의 중복을 막으려면?
  + ControllerAdvice를 활용하자 => Controller 이후의 영역에서 공통적 예외처리가 가능해짐

+ handler 객체 생성
```java
// handler/ControllerExceptionHandler.java

 @PutMapping("/{id}")
    public Response<Object> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        try {
            return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
        } catch (ApiException e){
            return Response.builder().code(e.getCode()).data(e.getMessage()).build();
        }
    }
```

+ ArticleController 객체의 try-catch문 제거
+ 동일하게 예외처리가 된 것을 확인 가능
```java
@PutMapping("/{id}")
    public Response<Object> put(@PathVariable Long id, @RequestBody ArticleDto.ReqPut request) {
        return Response.ok(ArticleDto.Res.of(articleService.update(Article.of(request, id))));
    }
```

+ 부가적인 예외처리
  1. Asserts 객체 생성(단언. 단언하다의 의미)
     1. 서비스쪽의 코드가 더 간단해짐
```java
// exception/Asserts.java

public class Asserts {
    public static void isNull(@Nullable Object obj, ApiCode code, String msg) {
        if(Objects.isNull(obj)) {
            throw new ApiException(code, msg);
        }
    }
}
```
```java
// service/ArticleService.java

@Transactional
    public Article update(Article request) {
        Article article = this.findById(request.getId());
        Asserts.isNull(article, ApiCode.DATA_IS_NOT_FOUND, "article is not existed");

        article.update(request.getTitle(), request.getContent());

        return article;
    }
```
  2. java에서 제공하는 방법
     1. Optional 객체를 사용해서 가독성을 높이는 방법
        1. null에 대한 대응을 명시적으로 만들어 줌
        2. 비용문제가 있기 때문에 중요한 비즈니스 로직에 사용하는 것을 권장함
```java
// service/ArticleService.java

     public Article findById(Long id) {
        return articleRepository.findById(id)
        .orElseThrow(() -> new ApiException(ApiCode.DATA_IS_NOT_FOUND, "article is not found"));
        }

@Transactional
public Article update(Article request) {
        Article article = this.findById(request.getId());

        article.update(request.getTitle(), request.getContent());

        return article;
        }
```