package com.soze.lifegameuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LifeGameUserApplication {

  public static void main(String[] args) {
    SpringApplication.run(LifeGameUserApplication.class, args);
  }

}
