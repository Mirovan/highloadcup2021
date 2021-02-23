package ru.bigint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bigint.model.Explore;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MapperUtils<T> {
    private Class<T> classType;

    public MapperUtils(Class<T> classType) {
        this.classType = classType;
    }

    private T stringToObject(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = objectMapper.readValue(json, classType);
        return obj;
    }

    public T convertToObject(String body) {
        T obj = null;
        try {
            obj = stringToObject(body);
            if ( obj != null) {
                if (obj instanceof String[]) {
                    String[] arr = (String[]) obj;
                    String strObj = Arrays.stream(arr).collect(Collectors.joining());
//                    Logger.log("Converted object: " + strObj);
                } else if (obj instanceof int[]) {
                    int[] arr = (int[]) obj;
                    String strObj = Arrays.stream(arr).mapToObj(item -> ((Integer) item).toString()).collect(Collectors.joining(", "));
//                    Logger.log("Converted object: " + strObj);
                } else {
//                    Logger.log("Converted object: " + obj);
                }
            } else {
//                Logger.log("Converted object is null");
            }
        } catch (Exception e) {
//            Logger.log("JSON convert to Object error: " + e.getMessage());
        }
        return obj;
    }

}
