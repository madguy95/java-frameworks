package io.jay.service.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<Member> members;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Milestone> milestones;

    @Version
    private Integer version = 0;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.milestones = new ArrayList<>();
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.setTeam(this);
    }

    public void addMilestone(Milestone milestone) {
        this.milestones.add(milestone);
        milestone.setTeam(this);
    }
}