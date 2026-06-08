package com.acme.catchup.platform.news.infraestructure.persistence.jpa.converters;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NewsApiKeyAttributeConverter implements AttributeConverter<NewsApiKey,String> {

    @Override
    public String convertToDatabaseColumn(NewsApiKey attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public NewsApiKey convertToEntityAttribute(String dbData) {
        return dbData == null? null : new NewsApiKey(dbData);
    }
}
