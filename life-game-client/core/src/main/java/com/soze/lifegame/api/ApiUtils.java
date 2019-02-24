package com.soze.lifegame.api;

import com.soze.lifegame.common.json.JsonUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiUtils {

  public static <T> T readObject(String json, Class<T> clazz) {
    return JsonUtils.jsonToObject(json, clazz);
  }

  public static <T> T readObject(CloseableHttpResponse response, Class<T> clazz) {
    return readObject(readEntity(response), clazz);
  }

  public static <T> T readObject(ApiResponse response, Class<T> clazz) {
    return readObject(response.getBody(), clazz);
  }

  public static <T> List<T> readList(String json, Class<T> clazz) {
    return JsonUtils.jsonToList(json, clazz);
  }

  public static <T> List<T> readList(CloseableHttpResponse response, Class<T> clazz) {
    return readList(readEntity(response), clazz);
  }

  public static <T> List<T> readList(ApiResponse response, Class<T> clazz) {
    return readList(response.getBody(), clazz);
  }

  public static String toString(Object obj) {
    return JsonUtils.objectToJson(obj);
  }

  public static String readEntity(CloseableHttpResponse response) {
    return readEntity(response.getEntity());
  }

  public static String readEntity(HttpEntity entity) {
    try {
      return IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static int getCode(CloseableHttpResponse response) {
    return response.getStatusLine().getStatusCode();
  }

  public static int getCode(ApiResponse response) {
    return response.getStatusCode();
  }

}
