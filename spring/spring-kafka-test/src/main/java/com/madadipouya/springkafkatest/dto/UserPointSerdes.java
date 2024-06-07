package com.madadipouya.springkafkatest.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Builder
@Data
public class UserPointSerdes  extends Serdes.WrapperSerde<UserPoint> {

    public UserPointSerdes() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(UserPoint.class));
    }

    public static Serde<UserPoint> serdes() {
        JsonSerializer<UserPoint> serializer = new JsonSerializer<>();
        JsonDeserializer<UserPoint> deserializer = new JsonDeserializer<>(UserPoint.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}