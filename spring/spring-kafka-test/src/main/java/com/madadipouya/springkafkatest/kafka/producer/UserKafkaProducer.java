package com.madadipouya.springkafkatest.kafka.producer;

import com.github.javafaker.Faker;
import com.madadipouya.springkafkatest.dto.Department;
import com.madadipouya.springkafkatest.dto.User;
import com.madadipouya.springkafkatest.dto.UserPoint;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Faker faker;
    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.topic.department}")
    private String topicDepartment;

    @Value("${spring.kafka.topic.string-constant}")
    private String topicString;

    @Value("${spring.kafka.topic.point}")
    private String topicPoint;

    @Value("${spring.kafka.replication.factor:1}")
    private int replicationFactor;

    @Value("${spring.kafka.partition.number:1}")
    private int partitionNumber;
    List<UserPoint> users = new ArrayList<>();

    public UserKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.faker = new Faker();
        users.add(new UserPoint(1L, faker.number().randomDouble(1, 0, 10),
                Instant.ofEpochMilli(faker.date().past(10, TimeUnit.DAYS).getTime()).atZone(ZoneId.systemDefault()).toLocalDate()));
        users.add(new UserPoint(2L, faker.number().randomDouble(1, 0, 10),
                Instant.ofEpochMilli(faker.date().past(10, TimeUnit.DAYS).getTime()).atZone(ZoneId.systemDefault()).toLocalDate()));
        users.add(new UserPoint(3L, faker.number().randomDouble(1, 0, 10),
                Instant.ofEpochMilli(faker.date().past(10, TimeUnit.DAYS).getTime()).atZone(ZoneId.systemDefault()).toLocalDate()));
//        users.add(new UserPoint(faker.random().nextLong(), faker.number().randomDouble(1, 0, 10),
//                Instant.ofEpochMilli(faker.date().past(10, TimeUnit.DAYS).getTime()).atZone(ZoneId.systemDefault()).toLocalDate()));
//        users.add(new UserPoint(faker.random().nextLong(), faker.number().randomDouble(1, 0, 10),
//                Instant.ofEpochMilli(faker.date().past(10, TimeUnit.DAYS).getTime()).atZone(ZoneId.systemDefault()).toLocalDate()));
    }

    @Transactional("kafkaTransactionManager")
    public void writeToKafka(User user) {
        kafkaTemplate.send(topic, user.getUuid(), user);
    }

    @Transactional("kafkaTransactionManager")
    public void writeToKafka(Department department) {
        kafkaTemplate.send(topicDepartment, department.getUuid(), department);
    }


    @Transactional("kafkaTransactionManager")
    public void writeToKafkaString(String s) {
        kafkaTemplate.send(topicString, s);
    }

    @Bean
    @Order(-1)
    public NewTopic createNewTopic() {
        return new NewTopic(topic, partitionNumber, (short) replicationFactor);
    }


    @Transactional("kafkaTransactionManager")
    @Scheduled(fixedRate = 10000)
    public void writeToKafkaPoint() {
        users.forEach((u) -> {
            u.setPoint(faker.number().randomDouble(1, 0, 10));
            kafkaTemplate.send(topicPoint, u);
        });
    }
}
