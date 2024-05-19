package io.jay.service.model.projection;

import org.springframework.beans.factory.annotation.Value;

public interface MemberView {
    @Value("#{target.id}")
    Long getMemberId();
    @Value("#{target.name}")
    String getMemberName();
}
