package io.jay.service.mongo.dto;

import io.jay.service.mongo.document.Player;
import io.jay.service.mongo.document.PlayerPosition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreatePlayerDto {
    private String name;

    private LocalDate birthDate;

    private PlayerPosition position;

    private boolean isAvailable;

    public Player toPlayer() {
        return new Player().setName(name).setBirthDate(birthDate).setPosition(position).setAvailable(isAvailable);
    }
}
