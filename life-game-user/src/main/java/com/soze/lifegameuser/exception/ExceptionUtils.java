package com.soze.lifegameuser.exception;

import com.soze.lifegame.common.api.ErrorResponse;
import com.soze.lifegame.common.json.JsonUtils;
import org.springframework.http.ResponseEntity;

public class ExceptionUtils {

  /**
   * Converts a [ErrorResponse] from the application into a [ResponseEntity].
   *
   * @param errorResponse response to convert
   * @return [ResponseEntity]
   */
  public static ResponseEntity<String> convertErrorResponse(ErrorResponse errorResponse) {
    return ResponseEntity
             .status(errorResponse.getStatusCode())
             .body(JsonUtils.objectToJson(errorResponse));
  }

}
