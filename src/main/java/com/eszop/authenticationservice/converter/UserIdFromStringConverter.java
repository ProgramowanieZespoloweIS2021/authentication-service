package com.eszop.authenticationservice.converter;

import com.eszop.authenticationservice.domain.UserId;
import org.springframework.core.convert.converter.Converter;

public class UserIdFromStringConverter implements Converter<Long, UserId>{
    @Override
    public UserId convert(Long value) {
        return UserId.of(value);
    }
}

