package com.zenin.genericproto.service;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenericJsonConverter {
    private final JsonFormat.Printer jsonMapper;

    public GenericJsonConverter(JsonFormat.Printer jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public String toJson(DynamicMessage event) {
        final String json = convertEventToJson(event);
        return enhanceJson(json, event.getAllFields());
    }

    private String enhanceJson(String json, Map<Descriptors.FieldDescriptor, Object> schema) {
        return json;
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
