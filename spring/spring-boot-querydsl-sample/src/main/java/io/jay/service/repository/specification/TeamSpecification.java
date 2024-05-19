package io.jay.service.repository.specification;

import io.jay.service.entity.jpa.MemberJPA;
import io.jay.service.entity.jpa.MemberJPA_;
import io.jay.service.entity.jpa.TeamJPA;
import io.jay.service.entity.jpa.TeamJPA_;
import jakarta.persistence.criteria.Fetch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;


public class TeamSpecification {
    public static final String PER = "%";

    public static Specification<TeamJPA> withMember(String memberName) {
        return (root, query, builder) -> {
            /*
                // query.multiselect(root.get(TeamJPA_.ID), root.get(TeamJPA_.NAME));
                Can't modify select when use specification
             */
            Fetch<TeamJPA, MemberJPA> fetchMember = root.fetch("members");
            return Objects.isNull(
                    memberName) ? builder.conjunction() :
                    builder.like(builder.lower(root.get(TeamJPA_.MEMBERS).get(MemberJPA_.NAME)),
                            String.join("", PER, StringUtils.lowerCase(memberName), PER));
        };
    }

    public static Specification<TeamJPA> withId(Long id) {
        return (root, query, builder) -> builder.equal(root.get(TeamJPA_.ID), id);
    }

    public static Specification<TeamJPA> withName(String name) {
        return (root, query, builder) -> Objects.isNull(
                name) ? builder.conjunction() : builder.like(builder.lower(root.get(TeamJPA_.name)),
                String.join("", PER, StringUtils.lowerCase(name), PER));
    }
}
