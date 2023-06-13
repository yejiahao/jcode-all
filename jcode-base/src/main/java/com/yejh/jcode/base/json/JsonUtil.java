package com.yejh.jcode.base.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-11
 * @since 1.0.0
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
        throw new AssertionError();
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static String serialize2String(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T deserialize2Object(String src, Class<T> clazz) throws IOException {
        return MAPPER.readValue(src, clazz);
    }
}
