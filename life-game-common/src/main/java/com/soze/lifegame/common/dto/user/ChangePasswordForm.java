package com.soze.lifegame.common.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ChangePasswordForm {

  private final char[] oldPassword;
  private final char[] newPassword;

  @JsonCreator
  public ChangePasswordForm(@JsonProperty("oldPassword") final char[] oldPassword,
                            @JsonProperty("newPassword") final char[] newPassword) {
    this.oldPassword = Objects.requireNonNull(oldPassword);
    this.newPassword = Objects.requireNonNull(newPassword);
  }

  public char[] getOldPassword() {
    return oldPassword;
  }

  public char[] getNewPassword() {
    return newPassword;
  }

  public void reset() {
    for (int i = 0; i < oldPassword.length; i++) {
      oldPassword[i] = 0;
    }
    for (int i = 0; i < newPassword.length; i++) {
      newPassword[i] = 0;
    }
  }

}
