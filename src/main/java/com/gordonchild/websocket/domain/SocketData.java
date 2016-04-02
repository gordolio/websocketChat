package com.gordonchild.websocket.domain;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gordonchild.websocket.util.JodaDateTimeDeserializer;
import com.gordonchild.websocket.util.JodaDateTimeSerializer;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
public class SocketData {

    @JsonSerialize(using=JodaDateTimeSerializer.class)
    @JsonDeserialize(using=JodaDateTimeDeserializer.class)
    private DateTime time = new DateTime();

    public DateTime getTime() {
        return this.time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }
}
