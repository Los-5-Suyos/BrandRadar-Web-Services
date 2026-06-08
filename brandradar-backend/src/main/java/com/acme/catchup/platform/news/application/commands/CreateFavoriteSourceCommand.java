package com.acme.catchup.platform.news.application.commands;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;

public record CreateFavoriteSourceCommand(NewsApiKey newsApiKey, SourceId sourceId) {

    public CreateFavoriteSourceCommand{
        if(newsApiKey==null){
            throw new IllegalArgumentException("NewsapiKye cannot be null");
        }
        if(sourceId==null){
            throw  new IllegalArgumentException("SourceId cannot be null");
        }

    }

}
