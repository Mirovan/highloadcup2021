package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.Explore;

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

    public T convertToObject(String body) {
        T obj = null;
        try {
            obj = stringToObject(body);
            Logger.log(obj);
        } catch (Exception e) {
            Logger.log("JSON convert to Object error: " + e.getMessage());
        }
        return obj;
    }

}
