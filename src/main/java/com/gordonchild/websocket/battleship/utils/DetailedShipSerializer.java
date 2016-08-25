package com.gordonchild.websocket.battleship.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gordonchild.websocket.battleship.domain.handler.Ship;

public class DetailedShipSerializer extends JsonSerializer<Ship> {

    @Override
    public void serialize(Ship value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if(value == null) {
            gen.writeNull();
            return;
        }
        gen.writeStartObject();
        gen.writeObjectField("id", value.name());
        gen.writeObjectField("writtenName", value.getWrittenName());
        gen.writeObjectField("size", value.getSize());
        gen.writeEndObject();
    }
}
