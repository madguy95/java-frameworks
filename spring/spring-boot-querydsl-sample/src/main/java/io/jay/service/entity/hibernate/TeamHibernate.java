package io.jay.service.entity.hibernate;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teams")
@JsonInclude(JsonInclude.Include.NON_NULL)
@org.hibernate.annotations.NamedQuery(name = "FIND_TEAMS", query = """
        SELECT t
            FROM TeamHibernate t
            LEFT JOIN FETCH t.members m
        WHERE t.id = :id 
            and (:name is null OR lower(t.name) like '%' || :name || '%' )
            and (:memberName is null OR lower(m.name) like '%' || :memberName || '%')
        """)
@org.hibernate.annotations.NamedQuery(name = "FIND_TEAMS_ONLY", query = """
        SELECT t.id, t.name
            FROM TeamHibernate t
            LEFT JOIN (
                SELECT m.team.id as teamId FROM MemberHibernate m) AS mh
            ON t.id = mh.teamId
        """)
public class TeamHibernate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<MemberHibernate> members;

    @Version
    private Integer version = 0;

}