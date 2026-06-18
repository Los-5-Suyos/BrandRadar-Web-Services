-- V1 already created Mention tied to Brand/MentionStream (old design). This migration evolves it
-- to the T-23 spec: drops the Brand/MentionStream relationship in favor of a direct workspaceId FK
-- to BrandWorkspace, replaces the 5-field sentiment breakdown with a single score + label, and adds
-- source/author/isActive. MEN_id (the PK) is left untouched since SentimentResult.MEN_id still
-- references it.

ALTER TABLE Mention DROP FOREIGN KEY mention_ibfk_1;
ALTER TABLE Mention DROP FOREIGN KEY mention_ibfk_2;

ALTER TABLE Mention
    ADD COLUMN BWS_id INT NOT NULL,
    ADD COLUMN MEN_source ENUM('FACEBOOK', 'TWITTER', 'TIKTOK', 'REDDIT', 'NEWS', 'YOUTUBE') NOT NULL,
    ADD COLUMN MEN_author_followers INT,
    ADD COLUMN MEN_sentiment_score DECIMAL(4,3) NOT NULL,
    ADD COLUMN MEN_sentiment_label ENUM('NEG', 'NEU', 'POS') NOT NULL,
    ADD COLUMN MEN_is_active TINYINT(1) NOT NULL DEFAULT 1,
    DROP COLUMN MES_id,
    DROP COLUMN BRA_id,
    DROP COLUMN MEN_source_platform,
    DROP COLUMN MEN_source_reliability,
    DROP COLUMN MEN_category,
    DROP COLUMN MEN_sentiment_positive,
    DROP COLUMN MEN_sentiment_negative,
    DROP COLUMN MEN_sentiment_neutral,
    DROP COLUMN MEN_sentiment_compound,
    DROP COLUMN MEN_sentiment_confidence,
    RENAME COLUMN MEN_source_url TO MEN_url,
    RENAME COLUMN MEN_author TO MEN_author_name,
    ADD CONSTRAINT fk_mention_workspace FOREIGN KEY (BWS_id) REFERENCES BrandWorkspace(BWS_id);

CREATE INDEX idx_mention_workspace_date ON Mention (BWS_id, MEN_published_at);
CREATE INDEX idx_mention_sentiment ON Mention (BWS_id, MEN_sentiment_label, MEN_published_at);
