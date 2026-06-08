package com.acme.catchup.platform.news.infraestructure.persistence.jpa.entities;

import com.acme.catchup.platform.news.domain.model.valueobjets.NewsApiKey;
import com.acme.catchup.platform.news.domain.model.valueobjets.SourceId;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.converters.NewsApiKeyAttributeConverter;
import com.acme.catchup.platform.news.infraestructure.persistence.jpa.converters.SourceIdAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "favorite_source", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"news_api_key","source_id"},name="uk_favorite_source_and_source_id")
})
public class FavoriteSourceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "news_api_key", nullable = false, length = 256)
    @Convert(converter = NewsApiKeyAttributeConverter.class)
    private NewsApiKey newsApiKey;
    @Column(name="source_id", nullable = false, length = 256)
    @Convert(converter = SourceIdAttributeConverter.class)
    private SourceId sourceId;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createAt;
    @Column(nullable = false,updatable = true)
    @LastModifiedDate
    private Instant updateUp;

}
