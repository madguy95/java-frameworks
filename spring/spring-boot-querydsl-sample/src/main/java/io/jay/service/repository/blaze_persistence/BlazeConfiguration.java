package io.jay.service.repository.blaze_persistence;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEntityViews("io.jay.service.repository.blaze_persistence")
public class BlazeConfiguration {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

//    @Bean
//    public EntityViewConfiguration createEntityViewConfiguration() {
//        EntityViewConfiguration config = EntityViews.createDefaultConfiguration();
//        return config;
//    }

    @Bean
    public EntityViewManager createEntityViewManager(
            CriteriaBuilderFactory criteriaBuilderFactory, EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(criteriaBuilderFactory);
    }
}
