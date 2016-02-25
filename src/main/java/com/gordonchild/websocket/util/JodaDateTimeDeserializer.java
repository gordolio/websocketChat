package com.gordonchild.websocket.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JodaDateTimeDeserializer extends JsonDeserializer<DateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'hh:mm:ss");

    @Override
    public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if(JsonToken.VALUE_NULL.equals(p.getCurrentToken())) {
            return null;
        } else {
            return FORMATTER.parseDateTime(p.getText());
        }
    }
}
