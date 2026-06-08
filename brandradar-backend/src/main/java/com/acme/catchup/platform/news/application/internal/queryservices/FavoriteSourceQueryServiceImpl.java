package com.acme.catchup.platform.news.application.internal.queryservices;

import com.acme.catchup.platform.news.application.queries.GetAllFavoriteSourceByNewsApiKeyQuery;
import com.acme.catchup.platform.news.application.queries.GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery;
import com.acme.catchup.platform.news.application.queries.GetFavoriteSourceByIdQuery;
import com.acme.catchup.platform.news.application.queryservices.FavoriteSourceQueryService;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;

import java.util.List;
import java.util.Optional;

public class FavoriteSourceQueryServiceImpl implements FavoriteSourceQueryService {
    @Override
    public List<FavoriteSource> handle(GetAllFavoriteSourceByNewsApiKeyQuery query) {
        return List.of();
    }

    @Override
    public Optional<FavoriteSource> handle(GetFavoriteSourceByIdQuery query) {
        return Optional.empty();
    }

    @Override
    public Optional<FavoriteSource> handle(GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery query) {
        return Optional.empty();
    }
}
