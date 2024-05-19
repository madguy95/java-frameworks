package io.jay.service.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jay.service.entity.QMember;
import io.jay.service.entity.QMilestone;
import io.jay.service.entity.QTeam;
import io.jay.service.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TeamQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<MemberVI.MemberTeamResponse> findMembers() {
        QMember memberTable = new QMember("member");
        QTeam teamTable = new QTeam("team");
        return queryFactory
                .select(
                        new QMemberVI_MemberTeamResponse(
                                memberTable.id,
                                memberTable.name,
                                new QTeamVI_TeamResponse(
                                        teamTable.id,
                                        teamTable.name,
                                        teamTable.members.size().castToNum(Long.class),
                                        teamTable.milestones.size().castToNum(Long.class))
                        )
                )
                .from(memberTable)
                .leftJoin(memberTable.team)
                .groupBy(memberTable.id)
                .fetch();
    }

    public List<TeamVI.TeamMemberResponse> findTeams() {
        QMember memberTable = new QMember("member");
        QTeam teamTable = new QTeam("team");
        return queryFactory
                .select(
                        teamTable
                )
                .from(teamTable)
                .leftJoin(teamTable.members).fetchJoin()
                .fetch()
                .stream().map(team -> new TeamVI.TeamMemberResponse(team)).toList();
//                .stream()
//                .map(tuple -> new Teams2Response(tuple.getId(),
//                        tuple.getName(),
//                        tuple.getMembers()
//                                .stream()
//                                .map(el -> new MemberResponse(el.getId(), el.getName()))
//                                .collect(Collectors.toList())))
//                .collect(Collectors.toList());
    }

    public List<MemberVI.MemberResponse> findMembersByTeamId(long teamId) {
        QMember memberTable = new QMember("member");

        return queryFactory
                .select(
                        new QMemberVI_MemberResponse(
                                memberTable.id,
                                memberTable.name
                        )
                )
                .from(memberTable)
                .where(
                        memberTable.team.id.eq(teamId)
                )
                .fetch();
    }


    public List<MemberVI.MemberResponse> searchMembersByTeamId(long teamId, String searchText) {
        QMember memberTable = new QMember("member");

        return queryFactory
                .select(
                        new QMemberVI_MemberResponse(
                                memberTable.id,
                                memberTable.name
                        )
                )
                .from(memberTable)
                .where(
                        memberTable.team.id.eq(teamId),
                        nameLike(searchText)
                )
                .fetch();
    }

    private BooleanExpression nameLike(String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return null;
        }

        QMember memberTable = new QMember("member");
        return memberTable.name.containsIgnoreCase(searchText);
    }


    public Page<MemberVI.MemberResponse> members(Pageable pageable) {
        QMember memberTable = new QMember("member");

        List<MemberVI.MemberResponse> members = queryFactory
                .select(
                        new QMemberVI_MemberResponse(
                                memberTable.id,
                                memberTable.name
                        )
                )
                .from(memberTable)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(members, pageable, () -> countQuery().fetchOne());
    }

    private JPAQuery<Long> countQuery() {
        QMember memberTable = new QMember("member");

        return queryFactory
                .select(memberTable.count())
                .from(memberTable);
    }

    /**
     * Join related table with fetch join
     *
     * @param id
     * @param name
     * @param member
     * @return List object after map from entity object
     */
    public List<TeamVI.TeamMemberResponse> findTeamByIDAndName(Long id, String name, String member) {
        QTeam teamTable = new QTeam("team");
        QMember memberTable = new QMember("member");
        BooleanBuilder builder = new BooleanBuilder();
        if (id != null) {
            builder.and(teamTable.id.eq(id));
        }
        if (name != null) {
            builder.and(teamTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", name).toString()));
        }
        if (member != null) {
            builder.and(memberTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", member).toString()));
        }

        return queryFactory
                .select(
                        teamTable
                )
                .from(teamTable)
                .leftJoin(teamTable.members, memberTable).fetchJoin()
                .where(builder)
                .fetch()
                .stream().map(team -> new TeamVI.TeamMemberResponse(team)).toList();
    }

    /**
     * Manual join tables without using fetch
     *
     * @param id
     * @param name
     * @param member
     * @return List Projection Object
     */
    public List<TeamVI.TeamMemberResponse> findTeamByIDAndNameProjection(Long id, String name, String member) {
        QTeam teamTable = new QTeam("team");
        QMember memberTable = new QMember("member");
        BooleanBuilder builder = new BooleanBuilder();
        if (id != null) {
            builder.and(teamTable.id.eq(id));
        }
        if (name != null) {
            builder.and(teamTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", name).toString()));
        }
        if (member != null) {
            builder.and(memberTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", member).toString()));
        }
        // V1
        /* Get all columns table
        return queryFactory
                .select(
                        teamTable,
                        memberTable
                )
                .from(teamTable)
                .leftJoin(memberTable)
                .on(teamTable.id.eq(memberTable.team.id))
                .where(builder)
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(tuple -> tuple.get(teamTable),
                        Collectors.mapping(tuple -> tuple.get(memberTable), Collectors.toList())))
                .entrySet()
                .stream().map(tuple -> new Teams2Response(tuple.getKey().getId(), tuple.getKey().getName(),
                        tuple.getValue().stream().map(el -> new MemberResponse(el)).toList()))
                .collect(Collectors.toList());
                */
        // v2 Get only specific columns
        return queryFactory
                .select(
                        new QTeamVI_TeamMemberResponse(teamTable.id, teamTable.name),
                        new QMemberVI_MemberResponse(memberTable.id, memberTable.name)
                )
                .from(teamTable)
                .leftJoin(memberTable)
                .on(teamTable.id.eq(memberTable.team.id))
                .where(builder)
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(tuple -> tuple.get(0, TeamVI.TeamMemberResponse.class),
                        Collectors.mapping(tuple -> tuple.get(1, MemberVI.MemberResponse.class), Collectors.toList())))
                .entrySet()
                .stream().map(tuple -> new TeamVI.TeamMemberResponse(tuple.getKey().id(), tuple.getKey().name(),
                        tuple.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Manual join tables without using fetch,
     * Using Subquery in from is not supported
     *
     * @param id
     * @param name
     * @param member
     * @return List Projection Object
     */
    public List<TeamVI.TeamMemberResponse> findTeamByIDAndNameSubQuery(Long id, String name, String member) {
        QTeam teamTable = new QTeam("team");
        QMember memberTable = new QMember("member");
        QMilestone milestoneTable = new QMilestone("milestone");
        BooleanBuilder builder = new BooleanBuilder();
        if (id != null) {
            builder.and(teamTable.id.eq(id));
        }
        if (name != null) {
            builder.and(teamTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", name).toString()));
        }
        if (member != null) {
            builder.and(memberTable.name.likeIgnoreCase(
                    Expressions.stringTemplate("%{0}%", member).toString()));
        }
        // v2 Get only specific columns
        return queryFactory
                .select(
                        new QTeamVI_TeamMemberResponse(teamTable.id, teamTable.name),
                        new QMemberVI_MemberResponse(memberTable.id, memberTable.name),
                        // Can use subquery in select clause (only get one column)
                        JPAExpressions.select(
                                        milestoneTable.id.count())
                                .from(milestoneTable).where(teamTable.id.eq(milestoneTable.team.id))
                )
                .from(teamTable)
                .leftJoin(memberTable)
                .on(teamTable.id.eq(memberTable.team.id))
                // Not support subquery in from
                /*
                    .leftJoin(JPAExpressions.select(milestoneTable)
                    .from(milestoneTable), milestoneTable)
                    .on(teamTable.id.eq(milestoneTable.team.id))
                */
                .where(builder.and(
                        // Can use subquery in where clause
                        JPAExpressions.select(
                                        milestoneTable.id, milestoneTable.name, milestoneTable.team.id)
                                .from(milestoneTable).where(teamTable.id.eq(milestoneTable.team.id)).exists()))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(tuple -> tuple.get(0, TeamVI.TeamMemberResponse.class),
                        Collectors.mapping(tuple -> tuple.get(1, MemberVI.MemberResponse.class), Collectors.toList())))
                .entrySet()
                .stream().map(tuple -> new TeamVI.TeamMemberResponse(tuple.getKey().id(), tuple.getKey().name(),
                        tuple.getValue()))
                .collect(Collectors.toList());
    }

}
