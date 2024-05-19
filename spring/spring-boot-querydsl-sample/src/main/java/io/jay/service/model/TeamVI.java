package io.jay.service.model;

import com.querydsl.core.annotations.QueryProjection;
import io.jay.service.entity.Team;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface TeamVI {
    record TeamResponse(
            long id,
            String name,
            long memberCount,
            long milestoneCount
    ) {
        @QueryProjection
        public TeamResponse {

        }
    }

    record TeamMemberResponse(
            long id,
            String name,
            List<MemberVI.MemberResponse> members
    ) {
        @QueryProjection
        public TeamMemberResponse {

        }

        public TeamMemberResponse(Team team) {
            this(team.getId(), team.getName(),
                    team.getMembers().stream().map(member -> new MemberVI.MemberResponse(member)).collect(
                            Collectors.toList()));
        }

        @QueryProjection
        public TeamMemberResponse(long id, String name) {
            this(id, name, Collections.emptyList());
        }
    }
}

