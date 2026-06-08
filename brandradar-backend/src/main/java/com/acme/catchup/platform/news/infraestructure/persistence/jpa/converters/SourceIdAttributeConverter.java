package com.acme.catchup.platform.news.infraestructure.persistence.jpa.converters;

import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SourceIdAttributeConverter implements AttributeConverter<SourceId,String> {

    @Override
    public String convertToDatabaseColumn(SourceId attribute) {
        return attribute == null ? null: attribute.value();
    }

    @Override
    public SourceId convertToEntityAttribute(String dbData) {
        return dbData==null? null: new SourceId(dbData);
    }
}
