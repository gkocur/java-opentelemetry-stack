package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersApplication {
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(UsersApplication.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }
}