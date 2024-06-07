package com.madadipouya.springkafkatest.kafka.processor;

import com.madadipouya.springkafkatest.dto.UserPoint;
import com.madadipouya.springkafkatest.dto.UserPointSerdes;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PointAverageProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Long> LONG_SERDE = Serdes.Long();
    private static final Serde<Double> DOUBLE_SERDE = Serdes.Double();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        final KStream<String, UserPoint> messageStream = streamsBuilder
                .stream("com.madadipouya.kafka.point", Consumed.with(STRING_SERDE, UserPointSerdes.serdes()));

        final KTable<Long, Double> processedStream = messageStream
                .map((key, value) -> new KeyValue<>(value.getUserId(), value.getPoint())) // Map từ UserPoint sang (userId, point)
                .groupByKey(Grouped.with(Serdes.Long(), DOUBLE_SERDE))
                .aggregate(
                        () -> new PointsAccumulator(0L, 0.0),
                        (key, value, aggregate) -> {
                            aggregate.setCount(aggregate.getCount() + 1);
                            aggregate.setSum(aggregate.getSum() + value);
                            return aggregate;
                        },
                        Materialized.with(LONG_SERDE, PointAccumulatorSerdes.serdes()) // Lưu trữ thay doi vào store
                ).mapValues(value -> value.getSum() / value.getCount());

        processedStream.toStream().foreach((key, value) -> {
//            System.out.println("Time: " + LocalDateTime.now().toString() + " User: " + key + ", Average Points: " + value);
        });

        processedStream.toStream().map(((key, value) -> new KeyValue<>(key, new UserPoint(key, value, LocalDate.now()))))
                .to("output-topic", Produced.with(LONG_SERDE, UserPointSerdes.serdes()));
    }


    @Autowired
    void buildPipelineNewest(StreamsBuilder streamsBuilder) {

        final KStream<String, UserPoint> messageStream = streamsBuilder
                .stream("com.madadipouya.kafka.point", Consumed.with(STRING_SERDE, UserPointSerdes.serdes()));

        final KTable<Windowed<Long>, UserPoint> processedStream = messageStream
                .map((key, value) -> new KeyValue<>(value.getUserId(), value)) // Map từ UserPoint sang (userId, point)
                .groupByKey(Grouped.with(Serdes.Long(), UserPointSerdes.serdes()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1))) // Cửa sổ thời gian 1 phút
                .reduce((value1, value2) -> {
                            // chi lay ban ghi moi nhat trong window
                            return value1.getPointDate().isAfter(value2.getPointDate()) ? value1 : value2;
                        },
                        Materialized.with(LONG_SERDE, UserPointSerdes.serdes()) // Lưu trữ thay doi vào store
                );

        processedStream.toStream().foreach((key, value) -> {
//            System.out.println("Time: " + LocalDateTime.now().toString() + " User: " + key.key() + ", Newest Points: " + value);
        });

        processedStream.toStream().map((key, value) -> new KeyValue<>(key.key(), value))
                .to("output-newest-topic", Produced.with(LONG_SERDE, UserPointSerdes.serdes()));
    }

    @Autowired
    void buildPipelineMaximum(StreamsBuilder streamsBuilder) {

        final KStream<String, UserPoint> messageStream = streamsBuilder
                .stream("com.madadipouya.kafka.point", Consumed.with(STRING_SERDE, UserPointSerdes.serdes()));

        final KTable<Long, Double> processedStream = messageStream
                .map((key, value) -> new KeyValue<>(value.getUserId(), value)) // Map từ UserPoint sang (userId, point)
                .groupByKey(Grouped.with(Serdes.Long(), UserPointSerdes.serdes()))
                .aggregate(
                        () -> Double.MIN_VALUE, // Initialize with smallest possible value
                        (key, value, aggregate) -> Math.max(value.getPoint(), aggregate), // Aggregator to find max point
                        Materialized.as("user-with-highest-points") // store to compare for all time
                );

        processedStream.toStream().foreach((key, value) -> {
            System.out.println("Time: " + LocalDateTime.now().toString() + " User: " + key + ", Maximum Points: " + value);
        });

        processedStream.toStream().map((key, value) -> new KeyValue<>(key, new UserPoint(key, value, LocalDate.now())))
                .to("output-maximum-topic", Produced.with(LONG_SERDE, UserPointSerdes.serdes()));
    }
}
