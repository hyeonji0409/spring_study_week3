package com.artineer.spring_study_week2.handler;

import com.artineer.spring_study_week2.dto.ArticleDto;
import com.artineer.spring_study_week2.dto.Response;
import com.artineer.spring_study_week2.exception.ApiException;
import com.artineer.spring_study_week2.vo.ApiCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public Response<String> apiException(ApiException e){
        return Response.<String>builder().code(e.getCode()).data(e.getMessage()).build();
    }
}
