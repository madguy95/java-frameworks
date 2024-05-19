package io.jay.service.model;

import com.querydsl.core.annotations.QueryProjection;
import io.jay.service.entity.Member;

public interface MemberVI {

    record MemberResponse(long id, String name) {

        @QueryProjection
        public MemberResponse {
        }

        public MemberResponse (Member member) {
            this( member.getId(), member.getName());
        }
    }

    record MemberTeamResponse(long id, String name, TeamVI.TeamResponse teamResponse) {

        @QueryProjection
        public MemberTeamResponse {
        }
    }
}
