package com.acme.catchup.platform.news.application.queries;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;

public record GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery(
        NewsApiKey newsApiKey, SourceId sourceId
) {
    public GetAllFavoriteSourceByNewsApikeyAndSourceIdQuery{

        if(newsApiKey==null){
            throw  new IllegalArgumentException("favorite.source.error.newsApiKey.invalid");
        }
        if(sourceId==null){
            throw  new IllegalArgumentException("favorite.source.error.sourceId.invalid");
        }

    }
}
