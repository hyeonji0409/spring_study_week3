package com.artineer.spring_study_week2.domain;

import com.artineer.spring_study_week2.dto.ArticleDto;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//실제 api로 내려가려면 표현의 영역을 거쳐 내려가야 한다 -> dto를 거침
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

    public void update(String title, String content) {
        this.title = title;
        this.content  = content;
    }

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
}
