package com.madadipouya.springkafkatest.kafka.processor;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Builder
@Data
public class PointAccumulatorSerdes extends Serdes.WrapperSerde<PointsAccumulator> {

    public PointAccumulatorSerdes() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(PointsAccumulator.class));
    }

    public static Serde<PointsAccumulator> serdes() {
        JsonSerializer<PointsAccumulator> serializer = new JsonSerializer<>();
        JsonDeserializer<PointsAccumulator> deserializer = new JsonDeserializer<>(PointsAccumulator.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}