package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
public class UsersController {
  private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

  @GetMapping("/")
  public ResponseEntity<String> index(@RequestParam("player") String player) {
    logger.info("Requested info for player: {} ", player);
    if(player.equalsIgnoreCase("john doe")) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Player is not allowed");
    }
    
    return ResponseEntity.ok().build();
  }

  
}