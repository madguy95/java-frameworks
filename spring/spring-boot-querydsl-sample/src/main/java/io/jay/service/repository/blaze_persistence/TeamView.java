package io.jay.service.repository.blaze_persistence;

import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.ContainsIgnoreCaseFilter;
import io.jay.service.entity.Member;
import io.jay.service.entity.Team;

import java.util.List;

@EntityView(Team.class)
public interface TeamView {

    @IdMapping("id")
    Long getTeamId();

    @Mapping("name")
    String getTeamName();

    @Mapping(value = "members", fetch = FetchStrategy.JOIN)
    List<MemberView> getMembers();

    @EntityView(Team.class)
    interface JoinTeamView {

        @IdMapping("id")
        Long getTeamId();

        @Mapping("name")
        @AttributeFilter(name = "name", value = ContainsIgnoreCaseFilter.class)
        String getTeamName();

        @MappingCorrelatedSimple(correlationBasis = "id",
                correlated = Member.class,
                fetch = FetchStrategy.JOIN,
                correlationKeyAlias= "t",
                correlationExpression = "team.id IN t")
        @AttributeFilter(name = "memberName", value = ContainsIgnoreCaseFilter.class) // not support
        List<MemberView> getListMembers();
    }

    @EntityView(Team.class)
    interface SimpleTeamView {

        @IdMapping("id")
        Long getTeamId();

        @Mapping("name")
        String getTeamName();

    }
}
