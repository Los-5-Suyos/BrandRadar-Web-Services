package com.acme.catchup.platform.news.infraestructure.persistence.jpa.adapters;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.repositories.FavoriteSourceRepository;
import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.mappers.FavoriteSourcePersistenceMapper;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.repositories.FavoriteSourceJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FavoriteSourcePersistenceAdapter implements
        FavoriteSourceRepository
{

    // Injection of dependencies for constructor
    private final FavoriteSourceJpaRepository repository;

    public FavoriteSourcePersistenceAdapter(FavoriteSourceJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public FavoriteSource save(FavoriteSource favoriteSource) {
        var entity = FavoriteSourcePersistenceMapper.toJpaEntity(favoriteSource);
        var savedEntity= repository.save(entity);
        return  FavoriteSourcePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<FavoriteSource> findById(Long id) {
        return repository.findById(id).map(FavoriteSourcePersistenceMapper::toDomain);
    }

    @Override
    public List<FavoriteSource> findAllByNewsApiKey(NewsApiKey newsApiKey) {
        return repository.findAllByNewsApiKey(newsApiKey).stream().map(
                FavoriteSourcePersistenceMapper::toDomain).toList();

    }

    @Override
    public Boolean existByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId) {
        return repository.existsByNewsApiKeyAndSourceId(newsApiKey,sourceId);
    }

    @Override
    public Optional<FavoriteSource> findByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId) {
        return repository.findByNewsApiKeyAndSourceId(newsApiKey,sourceId).map(
                FavoriteSourcePersistenceMapper::toDomain);
    }
}
