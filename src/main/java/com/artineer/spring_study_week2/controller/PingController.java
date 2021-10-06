package com.artineer.spring_study_week2.controller;

import com.artineer.spring_study_week2.dto.Response;
import com.artineer.spring_study_week2.vo.ApiCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Controller 라는 컴포넌트를 만들어서 client 의 요청을 받음(웹의 진입점)

//RestController => PingController 라는 객체를 controller 로 사용하겠다. (RestApi 의 형태로 구현하겠다)

@RestController
public class PingController {
    @GetMapping //요청할 경로 지정. 생략해도 무방함
    public ResponseEntity<Response<String>> ping() {
        Response<String> response = Response.<String>builder()
                .code(ApiCode.SUCESS).data("pong").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

        /*
        return Response.<String>builder()
                //생성자와 다르게 어느 값을 넣는지가 명시되어있음 => 인자가 많아도 생성자를 많이 만드는 이슈 발생 X
                //만들어진 후 수정할 필요가 없음 => 불변성 유지
                .code(ApiCode.SUCESS)
                .data("pong")
                .build();

         */
    }

    /*@GetMapping: 조회
    @PostMapping: 생성
    @PutMapping: 변경
    @DeleteMapping: 삭제*/
}
    /*
     return Map.of(
                    //Map 은 사실상 익명 객체라고 생각.
                    // -> 다른사람과 공유하면 응답을 만든건지 가독성이 떨어지기 때문에 클래스로 묶어서 내려주는게 효율적
                    //코드정보와 설명 정보를 적어줘야함.
                    "code", "0000",
                    "desc", "정상입니다",
                    "data", "pong"
            );
            */
            /*
            return new Response(ApiCode.SUCESS, "pong"); //각각의 결합도가 높은데 이어주지 않았기 때문에 중복의 이슈 발생
    */

//도메인 중심의 api를 생성하면 시스템 중심이기 때문에 굳이 만들지 않아도 원하는걸 찾을 수 있음. (RestApi)
// 클라이언트를 위한 api를 만들면 그 api를 다른 곳에 사용할 수 있을지는 알 수 없다. (




