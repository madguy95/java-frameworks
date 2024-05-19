package io.jay.service.repository.criteria;

import io.jay.service.config.ApplicationContextProvider;
import io.jay.service.model.projection.TeamDTO;

import java.util.List;

public interface TeamCriteriaRepository {

    default List<TeamDTO> retrieveTeamsByCriteria(Long id, String name, String member) {
        return ApplicationContextProvider.getContext().getBean(TeamCriteriaRepository.class).retrieveTeamsByCriteria(id,
                name, member);
    }

    default List<TeamDTO> retrieveTeamsByCriteriaProjection(Long id, String name, String member) {
        return ApplicationContextProvider.getContext().getBean(TeamCriteriaRepository.class).retrieveTeamsByCriteriaProjection(
                id, name, member);
    }
}
