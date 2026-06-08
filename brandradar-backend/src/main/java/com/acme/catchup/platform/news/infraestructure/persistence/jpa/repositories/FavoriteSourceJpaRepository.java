package com.acme.catchup.platform.news.infraestructure.persistence.jpa.repositories;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.entities.FavoriteSourceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSourceJpaRepository extends
        JpaRepository<FavoriteSourceJpaEntity, Long> {

    List<FavoriteSourceJpaEntity> findAllByNewsApiKey(NewsApiKey newsApiKey);
    Boolean existsByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId);
    Optional<FavoriteSourceJpaEntity> findByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId);
}
