package com.microsoft.cse.reference.spring.dal.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class IntegerToBoolean implements Converter<Integer,Boolean> {
    @Override
    public Boolean convert(Integer integer) {
        return !(integer == null || integer == 0);
    }
}