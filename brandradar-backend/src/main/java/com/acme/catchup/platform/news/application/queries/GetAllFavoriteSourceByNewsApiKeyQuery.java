package com.acme.catchup.platform.news.application.queries;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;

public record GetAllFavoriteSourceByNewsApiKeyQuery(NewsApiKey newsApiKey) {


    public  GetAllFavoriteSourceByNewsApiKeyQuery{
        if(newsApiKey==null){
            throw new IllegalArgumentException("NewsApiKey cannot be null");
        }

    }

}
