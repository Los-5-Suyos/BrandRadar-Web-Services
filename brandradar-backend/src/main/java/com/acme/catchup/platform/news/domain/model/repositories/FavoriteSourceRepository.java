package com.acme.catchup.platform.news.domain.model.repositories;

import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FavoriteSourceRepository {
   FavoriteSource save(FavoriteSource favoriteSource);
   Optional<FavoriteSource>  findById(Long id);
   List<FavoriteSource> findAllByNewsApiKey(NewsApiKey newsApiKey);
   Boolean existByNewsApiKeyAndSourceId(NewsApiKey newsApiKey, SourceId sourceId);
   Optional<FavoriteSource> findByNewsApiKeyAndSourceId(NewsApiKey newsApiKey,SourceId sourceId);
}
