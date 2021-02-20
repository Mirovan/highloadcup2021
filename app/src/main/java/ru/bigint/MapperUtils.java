package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtils<T> {
    private Class<T> classType;

    public MapperUtils(Class<T> classType) {
        this.classType = classType;
    }

    public T stringToObject(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = objectMapper.readValue(json, classType);
        return obj;
    }

}
