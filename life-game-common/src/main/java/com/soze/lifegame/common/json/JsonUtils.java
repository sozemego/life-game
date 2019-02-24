package com.soze.lifegame.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }

  /**
   * Attempts to stringify a given object to json.
   * If the conversion fails, wraps [JsonProcessingException] in [IllegalArgumentException].
   */
  public static String objectToJson(Object object) {
    return objectToJson(object, MAPPER);
  }

  /**
   * Attempts to stringify a given object to json.
   * If the conversion fails, wraps [JsonProcessingException] in [IllegalArgumentException].
   */
  public static String objectToJson(Object object, ObjectMapper mapper) {
    Objects.requireNonNull(object);
    Objects.requireNonNull(mapper);

    try {
      return MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      //TODO throw own exception
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Parses a given string and attempts to construct an object of a given class.
   * If the conversion fails, wraps [IOException] in [IllegalArgumentException].
   */
  public static <T> T jsonToObject(String json, Class<T> target) {
    try {
      return MAPPER.readValue(json, target);
    } catch (IOException e) {
      //TODO throw own exception
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T streamToObject(InputStream stream, Class<T> target) {
    try {
      return MAPPER.readValue(stream, target);
    } catch (IOException e) {
      //TODO throw own exception
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> List<T> jsonToList(String json, Class<T> target) {
    try {
      return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, target));
    } catch (IOException e) {
      //TODO throw own exception
      throw new IllegalArgumentException(e);
    }
  }

  public static <T, E> Map<T, E> jsonToMap(String json, Class<T> key, Class<E> value) {
    try {
      return MAPPER.readValue(json, MAPPER.getTypeFactory().constructMapType(HashMap.class, key, value));
    } catch (IOException e) {
      //TODO throw own exception
      throw new IllegalArgumentException(e);
    }
  }

}
