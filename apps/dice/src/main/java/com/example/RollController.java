package com.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class RollController {
  private static final Logger logger = LoggerFactory.getLogger(RollController.class);

  @Value("${usersUrl}")
  private String usersUrl;

  @GetMapping("/rolldice")
  public ResponseEntity<String> index(@RequestParam("player") Optional<String> player) {

    int result = this.getRandomNumber(1, 6);
    if (player.isPresent()) {
      logger.info("{} is rolling the dice: {}", player.get(), result);
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(java.net.URI.create(usersUrl + "?player=" + encodeValue(player.get())))
          .build();
      try {
        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 403 ) {
          logger.error("Player {} is not allowed to play", player.get());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Player is not allowed");
        }
        else {
          logger.info("Player {} is allowed to play", player.get());
          return ResponseEntity.ok().body(Integer.toString(result));
        }

      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
      
    } else {
      logger.info("Anonymous player is rolling the dice: {}", result);
    }
    return ResponseEntity.ok().body(Integer.toString(result));
  }

  public int getRandomNumber(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  private String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}