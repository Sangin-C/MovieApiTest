package com.locky.moveapitest.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;


@Slf4j
public class GenericJsonConverter<T> implements AttributeConverter<T, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(T additionalData) {
        // AdditionalData -> Json문자열로 변환
        try {
            return objectMapper.writeValueAsString(additionalData);
        } catch (JsonProcessingException e) {
            log.error("fail to serialize as object into Json : {}", additionalData, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public T convertToEntityAttribute(String jsonStr) {
        // Json 문자열 -> AdditionalData로 변환
        try {
            return objectMapper.readValue(jsonStr, new TypeReference<T>() {
            });
        } catch (IOException e) {
            log.error("fail to deserialize as Json into Object : {}", jsonStr, e);
            throw new RuntimeException(e);
        }
    }


}
