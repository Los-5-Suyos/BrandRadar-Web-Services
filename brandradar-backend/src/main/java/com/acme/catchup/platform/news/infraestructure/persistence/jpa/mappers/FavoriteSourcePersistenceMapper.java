package com.acme.catchup.platform.news.infraestructure.persistence.jpa.mappers;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.entities.FavoriteSourceJpaEntity;

public class FavoriteSourcePersistenceMapper {

    public static FavoriteSource toDomain(FavoriteSourceJpaEntity entity){

        return  FavoriteSource.rehydrate(
                entity.getId(),
                entity.getNewsApiKey(),
                entity.getSourceId(),
                entity.getCreateAt(),
                entity.getUpdateUp()
        );

    }
    public static  FavoriteSourceJpaEntity toJpaEntity(FavoriteSource aggregate){
        return new FavoriteSourceJpaEntity(
                aggregate.getId(),
                aggregate.getNewsApiKey(),
                aggregate.getSourceId(),
                aggregate.getCreateAt(),
                aggregate.getUpdateAt()

        );

    }





}
