package com.acme.catchup.platform.news.application.internal.commandservices;

import com.acme.catchup.platform.news.application.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.application.commandservices.FavoriteSourceCommandService;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.news.domain.model.repositories.FavoriteSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteSourceCommandServiceImpl implements FavoriteSourceCommandService {

    private FavoriteSourceRepository favoriteSourceRepository;

    public FavoriteSourceCommandServiceImpl(FavoriteSourceRepository favoriteSourceRepository) {
        this.favoriteSourceRepository = favoriteSourceRepository;
    }

    @Override
    @Transactional
    public FavoriteSource handle(CreateFavoriteSourceCommand command) {
        FavoriteSource createdFavoriteSource=null;
        if(favoriteSourceRepository.existByNewsApiKeyAndSourceId(
                command.newsApiKey(), command.sourceId()
        )){
            throw new IllegalArgumentException("Duplicate Favorite Source");
        }
        try{
            var favoriteSource = FavoriteSource.create(command.newsApiKey(),
                    command.sourceId());
            createdFavoriteSource=favoriteSourceRepository.save(favoriteSource);
        } catch (Exception e) {
            System.out.println("Error al insertar :"+e.getMessage());
        }
        return createdFavoriteSource;
    }

}
