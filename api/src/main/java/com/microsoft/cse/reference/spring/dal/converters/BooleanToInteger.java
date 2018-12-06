package com.microsoft.cse.reference.spring.dal.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class BooleanToInteger implements Converter<Boolean,Integer> {
    @Override
    public Integer convert(Boolean bool) {
        return bool == true ? 1 : 0;
    }
}
