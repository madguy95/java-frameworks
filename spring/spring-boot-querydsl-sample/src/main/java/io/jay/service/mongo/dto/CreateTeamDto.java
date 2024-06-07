package io.jay.service.mongo.dto;

import io.jay.service.mongo.document.Address;
import io.jay.service.mongo.document.TeamMg;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTeamDto {
    private String name;

    private String acronym;

    private Address address;

    public TeamMg toTeam() {
        return new TeamMg().setName(name).setAcronym(acronym).setAddress(address);
    }
}
