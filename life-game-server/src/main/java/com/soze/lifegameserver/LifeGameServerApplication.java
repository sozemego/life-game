package com.soze.lifegameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class LifeGameServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(LifeGameServerApplication.class, args);
  }

}
