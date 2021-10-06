package com.artineer.spring_study_week2.dto;

import com.artineer.spring_study_week2.vo.ApiCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
    private ApiCode code;
    private T data;

    public static Response<Void> ok() {
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
}
