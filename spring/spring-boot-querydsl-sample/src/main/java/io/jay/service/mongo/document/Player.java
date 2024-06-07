package io.jay.service.mongo.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "players")
@Accessors(chain = true)
@NoArgsConstructor
@Data
public class Player extends BaseModel {

    @Indexed
    private String name;

    private LocalDate birthDate;

    @Indexed
    @Field(targetType = FieldType.STRING)

    private PlayerPosition position;

    private boolean isAvailable;

}
