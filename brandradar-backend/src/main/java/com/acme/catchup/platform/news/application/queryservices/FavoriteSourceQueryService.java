package com.acme.catchup.platform.news.application.queryservices;

import com.acme.catchup.platform.news.application.queries.GetAllFavoriteSourceByNewsApiKeyQuery;
import com.acme.catchup.platform.news.application.queries.GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery;
import com.acme.catchup.platform.news.application.queries.GetFavoriteSourceByIdQuery;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;

import java.util.List;
import java.util.Optional;

public interface FavoriteSourceQueryService {
    List<FavoriteSource> handle(GetAllFavoriteSourceByNewsApiKeyQuery query);
    Optional<FavoriteSource> handle(GetFavoriteSourceByIdQuery query);
    Optional<FavoriteSource> handle(GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery query);
}
