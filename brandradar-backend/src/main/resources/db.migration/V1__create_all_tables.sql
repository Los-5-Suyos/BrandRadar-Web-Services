
CREATE TABLE IF NOT EXISTS UserAccount (
    USU_id            INT AUTO_INCREMENT PRIMARY KEY,
    USU_email         VARCHAR(255) NOT NULL UNIQUE,
    USU_password_hash VARCHAR(255) NOT NULL,
    USU_role          ENUM('ADMIN', 'ANALYST', 'VIEWER') NOT NULL DEFAULT 'ANALYST',
    USU_description   TEXT,
    USU_status        ENUM('PENDING_VERIFICATION', 'ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'PENDING_VERIFICATION',
    USU_created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    USU_updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS EmailVerification (
    EMV_id         INT AUTO_INCREMENT PRIMARY KEY,
    USU_id         INT          NOT NULL,
    EMV_token      VARCHAR(255) NOT NULL UNIQUE,
    EMV_status     ENUM('PENDING', 'USED', 'EXPIRED') NOT NULL DEFAULT 'PENDING',
    EMV_expires_at TIMESTAMP    NOT NULL,
    EMV_used_at    TIMESTAMP,
    EMV_created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS LoginAttempt (
    LGA_id           INT AUTO_INCREMENT PRIMARY KEY,
    USU_id           INT,
    LGA_email        VARCHAR(255) NOT NULL,
    LGA_ip_address   VARCHAR(45),
    LGA_success      TINYINT(1)   NOT NULL DEFAULT 0,
    LGA_attempted_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS PasswordRecovery (
    PWR_id         INT AUTO_INCREMENT PRIMARY KEY,
    USU_id         INT          NOT NULL,
    PWR_token      VARCHAR(255) NOT NULL UNIQUE,
    PWR_status     ENUM('PENDING', 'USED', 'EXPIRED') NOT NULL DEFAULT 'PENDING',
    PWR_expires_at TIMESTAMP    NOT NULL,
    PWR_used_at    TIMESTAMP,
    PWR_created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS AuthSession (
    AUS_id             INT AUTO_INCREMENT PRIMARY KEY,
    USU_id             INT          NOT NULL,
    AUS_token_jwt      TEXT         NOT NULL,
    AUS_refresh_token  VARCHAR(512) NOT NULL,
    AUS_ip_address     VARCHAR(45),
    AUS_expires_at     TIMESTAMP    NOT NULL,
    AUS_issued_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    AUS_invalidated_at TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS UnauthorizedAccessLog (
    UAL_id          INT AUTO_INCREMENT PRIMARY KEY,
    USU_id          INT,
    UAL_resource    VARCHAR(500) NOT NULL,
    UAL_ip_address  VARCHAR(45),
    UAL_occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS BrandWorkspace (
    BWS_id                  INT AUTO_INCREMENT PRIMARY KEY,
    USU_id                  INT          NOT NULL,
    BWS_name                VARCHAR(255) NOT NULL,
    BWS_description         VARCHAR(255),
    BWS_plan                ENUM('FREE', 'PRO', 'ENTERPRISE') NOT NULL DEFAULT 'FREE',
    BWS_status              ENUM('WITHOUT_CONFIGURATION', 'MONITORING_ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'WITHOUT_CONFIGURATION',
    BWS_policy_max_brands   INT NOT NULL DEFAULT 3,
    BWS_policy_alert_quota  INT NOT NULL DEFAULT 100,
    BWS_created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BWS_updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS WorkspaceAllowedChannel (
     WAC_id           INT AUTO_INCREMENT PRIMARY KEY,
     BWS_id           INT NOT NULL,
    WAC_channel_type ENUM('TWITTER', 'INSTAGRAM', 'FACEBOOK', 'NEWS', 'REDDIT', 'TIKTOK') NOT NULL,
    FOREIGN KEY (BWS_id) REFERENCES BrandWorkspace(BWS_id)
    );

CREATE TABLE IF NOT EXISTS Brand (
    BRA_id                       INT AUTO_INCREMENT PRIMARY KEY,
    BWS_id                       INT          NOT NULL,
    BRA_name                     VARCHAR(255) NOT NULL,
    BRA_reputation_score         DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    BRA_reputation_calculated_at TIMESTAMP,
    BRA_created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BRA_updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BWS_id) REFERENCES BrandWorkspace(BWS_id)
    );

CREATE TABLE IF NOT EXISTS KeywordRule (
    KWR_id         INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id         INT          NOT NULL,
    KWR_keyword    VARCHAR(255) NOT NULL,
    KWR_match_type ENUM('EXACT', 'PARTIAL', 'REGEX') NOT NULL DEFAULT 'PARTIAL',
    KWR_weight     DECIMAL(4,2) NOT NULL DEFAULT 1.00,
    KWR_is_active  TINYINT(1)   NOT NULL DEFAULT 1,
    KWR_created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS MonitoringChannel (
    MOC_id              INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id              INT  NOT NULL,
    MOC_channel_type    ENUM('TWITTER', 'INSTAGRAM', 'FACEBOOK', 'NEWS', 'REDDIT', 'TIKTOK') NOT NULL,
    MOC_status          ENUM('CONNECTED', 'DISCONNECTED', 'ERROR', 'CRITICAL') NOT NULL DEFAULT 'DISCONNECTED',
    MOC_credentials_enc TEXT,
    MOC_is_active       TINYINT(1) NOT NULL DEFAULT 1,
    MOC_last_sync_at    TIMESTAMP,
    MOC_created_at      TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    MOC_updated_at      TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS MentionStream (
    MES_id          INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id          INT       NOT NULL,
    MES_period_from TIMESTAMP NOT NULL,
    MES_period_to   TIMESTAMP NOT NULL,
    MES_status      ENUM('PROCESSING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PROCESSING',
    MES_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    MES_updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS Mention (
    MEN_id                   INT AUTO_INCREMENT PRIMARY KEY,
    MES_id                   INT,
    BRA_id                   INT           NOT NULL,
    MEN_content              TEXT          NOT NULL,
    MEN_source_platform      VARCHAR(100),
    MEN_source_url           VARCHAR(2000),
    MEN_source_reliability   DECIMAL(3,2)  NOT NULL DEFAULT 0.50,
    MEN_author               VARCHAR(255),
    MEN_published_at         TIMESTAMP,
    MEN_category             ENUM('POSITIVE', 'NEGATIVE', 'NEUTRAL', 'MIXED'),
    MEN_sentiment_positive   DECIMAL(5,4)  NOT NULL DEFAULT 0.0000,
    MEN_sentiment_negative   DECIMAL(5,4)  NOT NULL DEFAULT 0.0000,
    MEN_sentiment_neutral    DECIMAL(5,4)  NOT NULL DEFAULT 0.0000,
    MEN_sentiment_compound   DECIMAL(5,4)  NOT NULL DEFAULT 0.0000,
    MEN_sentiment_confidence DECIMAL(5,4)  NOT NULL DEFAULT 0.0000,
    MEN_created_at           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (MES_id) REFERENCES MentionStream(MES_id),
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS ReputationIncident (
    RIN_id                 INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id                 INT          NOT NULL,
    MES_id                 INT,
    RIN_severity_level     INT          NOT NULL DEFAULT 1,
    RIN_severity_label     VARCHAR(50),
    RIN_status             ENUM('ACTIVE', 'UNASSIGNED', 'RESPONDED', 'RESOLVED', 'DISCARDED') NOT NULL DEFAULT 'UNASSIGNED',
    RIN_assigned_to        INT,
    RIN_impact_score       DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    RIN_resolution_summary TEXT,
    RIN_resolution_actions TEXT,
    RIN_resolved_by        INT,
    RIN_resolved_at        TIMESTAMP,
    RIN_created_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    RIN_updated_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id)          REFERENCES Brand(BRA_id),
    FOREIGN KEY (MES_id)          REFERENCES MentionStream(MES_id),
    FOREIGN KEY (RIN_assigned_to) REFERENCES UserAccount(USU_id),
    FOREIGN KEY (RIN_resolved_by) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS IncidentEvent (
    IEV_id           INT AUTO_INCREMENT PRIMARY KEY,
    RIN_id           INT NOT NULL,
    IEV_event_type   ENUM('CREATED', 'ASSIGNED', 'STATUS_CHANGED', 'COMMENT_ADDED', 'RESOLVED', 'DISCARDED','REOPENED') NOT NULL,
    IEV_status       ENUM('PENDING', 'DONE', 'FAILED') NOT NULL DEFAULT 'DONE',
    IEV_performed_by INT,
    IEV_occurred_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (RIN_id)           REFERENCES ReputationIncident(RIN_id),
    FOREIGN KEY (IEV_performed_by) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS MonitoringRule (
    MOR_id                               INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id                               INT          NOT NULL,
    MOR_name                             VARCHAR(255) NOT NULL,
    MOR_is_active                        TINYINT(1)   NOT NULL DEFAULT 1,
    MOR_threshold_mention_volume_limit   INT          NOT NULL DEFAULT 100,
    MOR_threshold_negative_sentiment_pct DECIMAL(4,2) NOT NULL DEFAULT 0.50,
    MOR_threshold_time_window_minutes    INT          NOT NULL DEFAULT 60,
    MOR_notif_cooldown_minutes           INT          NOT NULL DEFAULT 30,
    MOR_created_at                       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    MOR_updated_at                       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS MonitoringRuleChannel (
    MRC_id           INT AUTO_INCREMENT PRIMARY KEY,
    MOR_id           INT NOT NULL,
    MRC_channel_type ENUM('TWITTER', 'INSTAGRAM', 'FACEBOOK', 'NEWS', 'REDDIT', 'TIKTOK') NOT NULL,
    MRC_is_active    TINYINT(1) NOT NULL DEFAULT 1,
    FOREIGN KEY (MOR_id) REFERENCES MonitoringRule(MOR_id)
    );

CREATE TABLE IF NOT EXISTS MonitoringRuleNotification (
    MRN_id                   INT AUTO_INCREMENT PRIMARY KEY,
    MOR_id                   INT NOT NULL,
    MRN_notification_channel ENUM('EMAIL', 'SLACK', 'WEBHOOK', 'SMS') NOT NULL,
    FOREIGN KEY (MOR_id) REFERENCES MonitoringRule(MOR_id)
    );

CREATE TABLE IF NOT EXISTS MonitoringRuleStakeholder (
    MRS_id INT AUTO_INCREMENT PRIMARY KEY,
    MOR_id INT NOT NULL,
    USU_id INT NOT NULL,
    FOREIGN KEY (MOR_id) REFERENCES MonitoringRule(MOR_id),
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS CrisisAlert (
    CRA_id                    INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id                    INT          NOT NULL,
    MES_id                    INT,
    MOR_id                    INT,
    CRA_priority_level        INT          NOT NULL DEFAULT 1,
    CRA_priority_label        VARCHAR(50),
    CRA_status                ENUM('OPEN', 'ACKNOWLEDGED', 'DISMISSED', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    CRA_trigger_type          ENUM('VOLUME', 'SENTIMENT', 'COMBINED'),
    CRA_trigger_deviation_pct DECIMAL(5,2),
    CRA_trigger_confidence    DECIMAL(5,2),
    CRA_detected_at           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CRA_acknowledged_at       TIMESTAMP,
    CRA_dismissed_reason      TEXT,
    CRA_response_time_minutes INT,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id),
    FOREIGN KEY (MES_id) REFERENCES MentionStream(MES_id),
    FOREIGN KEY (MOR_id) REFERENCES MonitoringRule(MOR_id)
    );

CREATE TABLE IF NOT EXISTS CrisisAlertNotifiedStakeholder (
    CNS_id          INT AUTO_INCREMENT PRIMARY KEY,
    CRA_id          INT NOT NULL,
    USU_id          INT NOT NULL,
    CNS_notified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CRA_id) REFERENCES CrisisAlert(CRA_id),
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS CrisisAlertAcknowledgement (
    CAA_id              INT AUTO_INCREMENT PRIMARY KEY,
    CRA_id              INT NOT NULL,
    USU_id              INT NOT NULL,
    CAA_acknowledged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CRA_id) REFERENCES CrisisAlert(CRA_id),
    FOREIGN KEY (USU_id) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS SentimentAnalysis (
    SEA_id                   INT AUTO_INCREMENT PRIMARY KEY,
    BRA_id                   INT          NOT NULL,
    SEA_period_from          TIMESTAMP    NOT NULL,
    SEA_period_to            TIMESTAMP    NOT NULL,
    SEA_score_positive       DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_score_negative       DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_score_neutral        DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_score_compound       DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_trend_direction      ENUM('IMPROVING', 'DECLINING', 'STABLE'),
    SEA_trend_magnitude      DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_delta_previous_score DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_delta_current_score  DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SEA_delta_change_pct     DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    SEA_created_at           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );

CREATE TABLE IF NOT EXISTS SentimentResult (
    SER_id               INT AUTO_INCREMENT PRIMARY KEY,
    SEA_id               INT NOT NULL,
    MEN_id               INT NOT NULL,
    SER_score_positive   DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SER_score_negative   DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SER_score_neutral    DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SER_score_compound   DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    SER_dominant_emotion ENUM('JOY', 'ANGER', 'SADNESS', 'FEAR', 'SURPRISE', 'DISGUST', 'NEUTRAL'),
    SER_language         VARCHAR(10),
    SER_analyzed_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (SEA_id) REFERENCES SentimentAnalysis(SEA_id),
    FOREIGN KEY (MEN_id) REFERENCES Mention(MEN_id)
    );

CREATE TABLE IF NOT EXISTS SentimentResultTheme (
    SRT_id    INT AUTO_INCREMENT PRIMARY KEY,
    SER_id    INT          NOT NULL,
    SRT_theme VARCHAR(255) NOT NULL,
    FOREIGN KEY (SER_id) REFERENCES SentimentResult(SER_id)
    );

CREATE TABLE IF NOT EXISTS ReputationReport (
    RPR_id           INT AUTO_INCREMENT PRIMARY KEY,
    BWS_id           INT          NOT NULL,
    BRA_id           INT,
    RPR_title        VARCHAR(255) NOT NULL,
    RPR_period_from  TIMESTAMP    NOT NULL,
    RPR_period_to    TIMESTAMP    NOT NULL,
    RPR_status       ENUM('GENERATING', 'READY', 'FAILED') NOT NULL DEFAULT 'GENERATING',
    RPR_format       ENUM('PDF', 'CSV')                    NOT NULL DEFAULT 'PDF',
    RPR_file_url     VARCHAR(2000),
    RPR_generated_by INT,
    RPR_generated_at TIMESTAMP,
    RPR_created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BWS_id)           REFERENCES BrandWorkspace(BWS_id),
    FOREIGN KEY (BRA_id)           REFERENCES Brand(BRA_id),
    FOREIGN KEY (RPR_generated_by) REFERENCES UserAccount(USU_id)
    );

CREATE TABLE IF NOT EXISTS ReputationReportIncident (
    RRI_id  INT AUTO_INCREMENT PRIMARY KEY,
    RPR_id  INT NOT NULL,
    RIN_id  INT NOT NULL,
    FOREIGN KEY (RPR_id) REFERENCES ReputationReport(RPR_id) ON DELETE CASCADE,
    FOREIGN KEY (RIN_id) REFERENCES ReputationIncident(RIN_id)
    );

CREATE TABLE IF NOT EXISTS ServiceHealthCheck (
    SHC_id                       INT AUTO_INCREMENT PRIMARY KEY,
    SHC_service_name             VARCHAR(255)  NOT NULL,
    SHC_endpoint_url             VARCHAR(2000) NOT NULL,
    SHC_endpoint_method          VARCHAR(10)   NOT NULL DEFAULT 'GET',
    SHC_endpoint_timeout_ms      INT           NOT NULL DEFAULT 5000,
    SHC_status                   ENUM('HEALTHY', 'DEGRADED', 'DOWN') NOT NULL DEFAULT 'HEALTHY',
    SHC_uptime_total_checks      INT           NOT NULL DEFAULT 0,
    SHC_uptime_successful_checks INT           NOT NULL DEFAULT 0,
    SHC_uptime_window_days       INT           NOT NULL DEFAULT 30,
    SHC_last_checked_at          TIMESTAMP,
    SHC_created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    SHC_updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS HealthCheckResult (
    HCR_id               INT AUTO_INCREMENT PRIMARY KEY,
    SHC_id               INT        NOT NULL,
    HCR_response_time_ms BIGINT     NOT NULL DEFAULT 0,
    HCR_http_status      SMALLINT,
    HCR_is_healthy       TINYINT(1) NOT NULL DEFAULT 1,
    HCR_checked_at       TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (SHC_id) REFERENCES ServiceHealthCheck(SHC_id)
    );

CREATE TABLE IF NOT EXISTS InfraIncident (
    INI_id                            INT AUTO_INCREMENT PRIMARY KEY,
    SHC_id                            INT          NOT NULL,
    INI_incident_type                 ENUM('OUTAGE', 'DEGRADED', 'LATENCY', 'ERROR_SPIKE') NOT NULL,
    INI_severity_level                INT          NOT NULL DEFAULT 1,
    INI_severity_label                VARCHAR(50),
    INI_status                        ENUM('OPEN', 'INVESTIGATING', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    INI_estimated_reputational_impact DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    INI_resolution_summary            TEXT,
    INI_resolution_root_cause         TEXT,
    INI_resolution_preventive         TEXT,
    INI_detected_at                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INI_resolved_at                   TIMESTAMP,
    FOREIGN KEY (SHC_id) REFERENCES ServiceHealthCheck(SHC_id)
    );

CREATE TABLE IF NOT EXISTS InfraIncidentAffectedBrand (
    IAB_id     INT AUTO_INCREMENT PRIMARY KEY,
    INI_id     INT NOT NULL,
    BRA_id     INT NOT NULL,
    FOREIGN KEY (INI_id) REFERENCES InfraIncident(INI_id),
    FOREIGN KEY (BRA_id) REFERENCES Brand(BRA_id)
    );