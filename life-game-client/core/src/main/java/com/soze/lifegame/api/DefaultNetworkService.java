package com.soze.lifegame.api;

import com.soze.lifegame.common.api.ErrorResponse;
import com.soze.lifegame.exception.ApiException;
import com.soze.lifegame.exception.NetworkException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultNetworkService implements NetworkService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultNetworkService.class);

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  @Override
  public ApiResponse get(String path) {
    LOG.info("Making GET request to {}", path);
    HttpGet httpGet = new HttpGet(path);
    return handleNetworkError(() -> getApiResponse(httpClient.execute(httpGet)));
  }

  @Override
  public ApiResponse post(String path, String body) {
    LOG.info("Making a POST request to {} with body {}", path, body);
    HttpPost httpPost = new HttpPost(path);
    httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    return handleNetworkError(() -> getApiResponse(httpClient.execute(httpPost)));
  }

  @Override
  public ApiResponse post(String path, Object body) {
    return post(path, ApiUtils.toString(body));
  }

  private ApiResponse getApiResponse(CloseableHttpResponse response) throws ApiException {
    int code = ApiUtils.getCode(response);
    if (code > 399) {
      ErrorResponse error = ApiUtils.readObject(response, ErrorResponse.class);
      throw new ApiException(error);
    }
    return new ApiResponse(code, ApiUtils.readEntity(response));
  }

  /**
   * Executes given block which should make a HTTP call. If an IOException occurs,
   * it will be wrapped as NetworkException and rethrown.
   */
  private ApiResponse handleNetworkError(NetworkRequest<ApiResponse> request) {
    try {
      return request.get();
    } catch (IOException e) {
      e.printStackTrace();
      throw new NetworkException(e);
    } catch (ApiException e) {
      e.printStackTrace();
      throw e;
    } catch(Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
