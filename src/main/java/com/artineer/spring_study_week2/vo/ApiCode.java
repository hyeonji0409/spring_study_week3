package com.artineer.spring_study_week2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
//Response 에 있는 code, desc 의 결합도가 떨어지는 이슈를 해결하기 위함.
public enum ApiCode {
    /* CM COMMON */
    SUCESS("CM0000", "정상입니다" ),
    DATA_IS_NOT_FOUND("CM0001", "데이터가 존재하지 않습니다.");

    private final String name;
    private final String desc;

    ApiCode(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
/*
    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

 */
}
