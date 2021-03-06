package com.assetco.search.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Health {
  @GetMapping(
      value = "/health/status",
      produces = {"application/json"})
  public String check() {
    return "{\"up\":true}";
  }
}
