package com.artineer.spring_study_week2.exception;

import com.artineer.spring_study_week2.vo.ApiCode;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class Asserts {
    public static void isNull(@Nullable Object obj, ApiCode code, String msg) {
        if(Objects.isNull(obj)) {
            throw new ApiException(code, msg);
        }
    }
}
