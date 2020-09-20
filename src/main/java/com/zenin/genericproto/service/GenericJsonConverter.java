package com.zenin.genericproto.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenericJsonConverter {
  private final JsonFormat.Printer jsonMapper;
  private final Gson gson;

  public GenericJsonConverter(JsonFormat.Printer jsonMapper, GsonBuilder gsonBuilder) {
    this.jsonMapper = jsonMapper;
    this.gson = gsonBuilder.create();
  }

  public JsonObject toJson(DynamicMessage event) {
    final String json = convertEventToJson(event);
    return enhanceJson(gson.fromJson(json, JsonObject.class), event.getAllFields());
  }

  private JsonObject enhanceJson(JsonObject jsonObject, Map<Descriptors.FieldDescriptor, Object> schema) {
    return jsonObject;
  }

  private String convertEventToJson(DynamicMessage event) {
    String json;
    try {
      json = jsonMapper.print(event);
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException("Failed to convert event to JSON", e);
    }
    return json;
  }
}
