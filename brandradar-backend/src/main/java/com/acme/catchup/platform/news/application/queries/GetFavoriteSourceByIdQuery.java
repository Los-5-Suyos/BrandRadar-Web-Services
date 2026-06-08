package com.acme.catchup.platform.news.application.queries;

public record GetFavoriteSourceByIdQuery(Long id) {

    public GetFavoriteSourceByIdQuery{

        if(id==null || id <=0){
            throw new IllegalArgumentException("favorite.source.error.id.invalid");
        }

    }


}
