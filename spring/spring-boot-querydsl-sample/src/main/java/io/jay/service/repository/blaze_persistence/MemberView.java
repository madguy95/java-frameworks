package io.jay.service.repository.blaze_persistence;

import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.ContainsIgnoreCaseFilter;
import io.jay.service.entity.Member;

@EntityView(Member.class)
@ViewFilter(name = "memberFilter", value = MemberView.NameFilterProvider.class)
public interface MemberView {
    @IdMapping("id")
    Long getMemberId();

    @AttributeFilter(name = "memberName", value = ContainsIgnoreCaseFilter.class)
    @Mapping("name")
    String getMemberName();

    class NameFilterProvider extends ViewFilterProvider {
        @Override
        public <T extends WhereBuilder<T>> T apply(T whereBuilder) {
            return whereBuilder.where("LOWER(name)").like().value("%jay%").noEscape();
        }
    }
}
