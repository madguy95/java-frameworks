package io.jay.service.entity.jpa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teams")
public class TeamJPA implements Cloneable {

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

    @Version
    private Integer version = 0;

    public TeamJPA(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format("Team ID {%s} name {%s} version  {%s}", this.id, this.name, this.version);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}