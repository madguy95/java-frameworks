package io.jay.service.mongo.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Setter
@Getter
public abstract class BaseModel {
    @MongoId(FieldType.OBJECT_ID)
    protected String id;

    protected LocalDate createdAt;

    protected LocalDate updatedAt;
}
