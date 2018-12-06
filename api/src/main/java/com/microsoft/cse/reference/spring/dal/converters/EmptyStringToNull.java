package com.microsoft.cse.reference.spring.dal.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class EmptyStringToNull implements Converter<String,String> {
    @Override
    public String convert(String str) {
        return str == null || str.isEmpty() ? null : str;
    }
}
