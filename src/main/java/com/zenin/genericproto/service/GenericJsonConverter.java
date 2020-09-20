package com.zenin.genericproto.service;

import com.google.gson.*;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

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
    return enhanceJson(gson.fromJson(json, JsonObject.class), event);
  }

  private JsonObject enhanceJson(final JsonObject jsonObject, final DynamicMessage event) {
    Set<String> timestampLocations = getTimestampPaths(event.getAllFields());
    timestampLocations.forEach(location -> setUnixTime(jsonObject, location, event));
    return jsonObject;
  }

  private void setUnixTime(JsonObject jsonObject, String timestampLocation, DynamicMessage event) {
    final var entry = getJsonElementAtPath(timestampLocation, jsonObject);
    final Timestamp timestamp = getTimestamp(event, timestampLocation);
    entry.setValue(new JsonPrimitive(timestamp.getSeconds()));
  }

  Map.Entry<String, JsonElement> getJsonElementAtPath(String path, JsonObject jsonObject) {
    String[] split = path.split("\\.");
    if (split.length == 0) {
      split = new String[] {path};
    }
    JsonObject intermediate = jsonObject;
    Map.Entry<String, JsonElement> res = null;
    for (int i = 0; i < split.length; i++) {
      final var memberName = split[i];
      if (i == split.length - 1) {
        res =
            intermediate.entrySet().stream()
                .filter(entry -> entry.getKey().equals(memberName))
                .findFirst()
                .get();
        break;
      }
      intermediate = intermediate.getAsJsonObject(memberName);
    }

    return res;
  }

  Timestamp getTimestamp(final DynamicMessage event, final String path) {
    String[] split = path.split("\\.");
    if (split.length == 0) {
      split = new String[] {path};
    }

    Timestamp res = null;
    DynamicMessage intermediate = event;
    for (int i = 0; i < split.length; i++) {
      intermediate =
          (DynamicMessage)
              intermediate.getField(intermediate.getDescriptorForType().findFieldByName(split[i]));
      if (i == split.length - 1) {
        res = parseToTimestamp(intermediate);
        break;
      }
    }

    return res;
  }

  private Timestamp parseToTimestamp(DynamicMessage timestampDynamic) {
    Timestamp res;
    try {
      res = Timestamp.parseFrom(timestampDynamic.toByteArray());
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  private Set<String> getTimestampPaths(Map<Descriptors.FieldDescriptor, Object> schema) {
    return Set.of("time_of_reading");
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
