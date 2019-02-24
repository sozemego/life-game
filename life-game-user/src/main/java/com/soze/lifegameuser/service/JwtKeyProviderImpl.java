package com.soze.lifegameuser.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtKeyProviderImpl implements JwtKeyProvider {

  @Value("${KLEDDIT_JWT_SECRET}")
  private String secret;

  @Override
  public byte[] getSecret() {
    return secret.getBytes();
  }
}
