package io.jay.service.entity.nonlock;

import io.jay.service.entity.jpa.MemberJPA;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teams")
public class TeamNoLock implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<MemberJPA> members;


    public TeamNoLock(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format("Team ID {%s} name {%s}", this.id, this.name);
    }

    @Override
    public TeamNoLock clone() throws CloneNotSupportedException {
        return new TeamNoLock(this.name);
    }
}