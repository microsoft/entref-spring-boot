package com.microsoft.cse.reference.spring.dal.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.cse.reference.spring.dal.config.Constants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Parses json arrays and csv literals to string lists
 *
 * Note: we don't convert back, so this is also a data cleaning component - that is,
 * any new writes will have a normalized schema using an array, not a string literal
 *
 * If we didn't want this behavior, we'd need a more complicated @WritingConverter to match
 */
@Component
@ReadingConverter
public class JsonArrayToStringList implements Converter<String, List<String>> {
    @Override
    public List<String> convert(String str) {
        try {
            str = str.isEmpty() || str == null ? "[]" : str;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(str);
            
            if (!jsonNode.isArray()) {
                throw new IOException(Constants.ERR_TEST_DATA_FORMAT);
            } else {
                ArrayList<String> results = new ArrayList<>();

                Iterator<JsonNode> it = jsonNode.elements();
                while (it.hasNext()) {
                    results.add(it.next().asText());
                }

                return results;
            }
        } catch (IOException e) {
            if (str.contains(",")) {
                return Arrays.asList(str.split(","));
            } else {
                return Arrays.asList(str);
            }
        }
    }
}
