package com.artineer.spring_study_week2.dto;

import com.artineer.spring_study_week2.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//도메인과 프레젠테이션 영역이 구분되어야 하고, 구분하기 위해 사용하는 객체가 dto
public class ArticleDto {
    //생성 요청을 할 때 사용하는 객체 => 실제는 article로 변경되서 작용
    @Getter
    public static class ReqPost {
        String title;
        String content;
    }
    @Getter
    public static class ReqPut{
        String title;
        String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Res {
        private String id;
        private String title;
        private String content;

        public static Res of(Article from) {
            return Res.builder()
                    .id(String.valueOf(from.getId()))
                    .title(String.valueOf(from.getTitle()))
                    .content(String.valueOf(from.getContent()))
                    .build();
        }
    }
}
