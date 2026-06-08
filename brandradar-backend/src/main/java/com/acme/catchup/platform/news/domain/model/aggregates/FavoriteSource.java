package com.acme.catchup.platform.news.domain.model.aggregates;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;

import java.time.Instant;
import java.util.Objects;

public class FavoriteSource {

    private final Long id;
    private final NewsApiKey newsApiKey;
    private final SourceId sourceId;
    private final Instant createAt;
    private final Instant updateAt;

    private FavoriteSource(Long id, NewsApiKey newsApiKey,
                           SourceId sourceId, Instant createAt,
                           Instant updateAt){
        this.id=id;
        this.newsApiKey = Objects.requireNonNull(newsApiKey,"");
        this.sourceId = Objects.requireNonNull(sourceId,"");
        this.createAt = createAt;
        this.updateAt = updateAt;

    }
    public static FavoriteSource create(NewsApiKey newsApiKey, SourceId sourceId){
        return  new FavoriteSource(null,newsApiKey,sourceId,null,null);
    }

    public static FavoriteSource rehydrate(
            Long id, NewsApiKey newsApiKey,
            SourceId sourceId, Instant createAt,
            Instant updateAt
    ){
       return  new FavoriteSource(id,newsApiKey,sourceId,createAt,updateAt);

    }

    public Long getId() {
        return id;
    }

    public NewsApiKey getNewsApiKey() {
        return newsApiKey;
    }

    public SourceId getSourceId() {
        return sourceId;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }
}
