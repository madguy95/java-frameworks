package io.jay.service.model.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface TeamView {

    @Value("#{target.id}")
    Long getTeamId();
    @Value("#{target.name}")
    String getTeamName();
    List<MemberView> getMembers();

    interface TeamSubView {
        Long getId();
        String getName();
        String getMemberName();
    }
}
