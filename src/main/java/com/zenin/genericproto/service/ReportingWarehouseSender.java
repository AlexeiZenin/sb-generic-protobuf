package com.zenin.genericproto.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportingWarehouseSender {
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public void sendToWarehouse(String json) {
    // purely for demo purposes
    JsonObject parsed = gson.fromJson(json, JsonObject.class);
    log.info("\n{}", gson.toJson(parsed));
  }
}
