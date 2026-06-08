package com.acme.catchup.platform.news.application.commandservices;

import com.acme.catchup.platform.news.application.commands.CreateFavoriteSourceCommand;
import com.acme.catchup.platform.news.domain.model.aggregates.FavoriteSource;
import com.acme.catchup.platform.shared.application.result.Result;

public interface FavoriteSourceCommandService {
    FavoriteSource handle(CreateFavoriteSourceCommand command);
}
