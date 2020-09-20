package com.zenin.genericproto.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericJsonConverterTest {

  private GenericJsonConverter genericJsonConverter;

  @BeforeEach
  public void setup() {
    genericJsonConverter =
        new GenericJsonConverter(
            JsonFormat.printer().omittingInsignificantWhitespace(), new GsonBuilder());
  }

  @Test
  void getJsonElementAtPath_MutateSuccessful() {
    final var top = new JsonObject();
    final var aObject = new JsonObject();
    final var bObject = new JsonObject();
    bObject.addProperty("b", 123);
    aObject.add("a", bObject);

    top.add("test", aObject);

    final var entry = genericJsonConverter.getJsonElementAtPath("test.a.b", top);
    assertTrue(entry.getValue().isJsonPrimitive());
    assertTrue(entry.getKey().equals("b"));
    assertTrue(entry.getValue().getAsInt() == 123);

    entry.setValue(new JsonPrimitive(57L));

    final var entryAfterMutation = genericJsonConverter.getJsonElementAtPath("test.a.b", top);

    assertTrue(entryAfterMutation.getValue().isJsonPrimitive());
    assertTrue(entryAfterMutation.getKey().equals("b"));
    assertTrue(entryAfterMutation.getValue().getAsLong() == 57L);
  }
}
