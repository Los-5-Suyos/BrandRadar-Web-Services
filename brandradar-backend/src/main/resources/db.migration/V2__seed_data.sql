-- ============================================================
-- BrandRadar DB -  Data
-- ============================================================

-- Carla: PYME FREE - Netflix
INSERT INTO UserAccount (USU_email, USU_full_name, USU_username, USU_password_hash, USU_role, USU_description, USU_status, USU_verification_code)
VALUES ('carla@cevicheriadelmar.pe', 'Carla Mendoza', 'carla.mendoza', '$2a$10$DjPiSpPCp5XeENOpGVXUzOVwco3YNTMP.ShWQM.5hYQt5W9CH9UW.', 'PYME', 'PyME - Plan FREE', 'ACTIVE', '123456');

-- Jorge: AGENCIA FREE - Uber
INSERT INTO UserAccount (USU_email, USU_full_name, USU_username, USU_password_hash, USU_role, USU_description, USU_status, USU_verification_code)
VALUES ('jorge@reputaxagencia.pe', 'Jorge Ramírez', 'jorge.ramirez', '$2a$10$bkuLHLBee1UDeZkpA/WsfOVopJQAE8bq/FKNDp.da5iE6jHVk4TaS', 'AGENCIA', 'Agencia - Plan FREE', 'ACTIVE', '123456');

-- Workspace Carla: Netflix
INSERT INTO BrandWorkspace (USU_id, BWS_name, BWS_plan, BWS_status, BWS_policy_max_brands, BWS_policy_alert_quota)
VALUES (1, 'Netflix', 'FREE', 'MONITORING_ACTIVE', 1, 500);

-- Workspace Jorge: Uber
INSERT INTO BrandWorkspace (USU_id, BWS_name, BWS_plan, BWS_status, BWS_policy_max_brands, BWS_policy_alert_quota)
VALUES (2, 'Uber', 'FREE', 'MONITORING_ACTIVE', 1, 500);

-- Config Netflix
INSERT INTO WorkspaceConfig (BWS_id, WCF_company_name, WCF_industry, WCF_website_url, WCF_youtube_url)
VALUES (1, 'Netflix Inc.', 'Tecnología', 'https://www.netflix.com', 'https://www.youtube.com/@Netflix');

-- Config Uber
INSERT INTO WorkspaceConfig (BWS_id, WCF_company_name, WCF_industry, WCF_website_url, WCF_youtube_url)
VALUES (2, 'Uber Technologies', 'Tecnología', 'https://www.uber.com', 'https://www.youtube.com/@Uber');

-- Brand Netflix
INSERT INTO Brand (BWS_id, BRA_name, BRA_reputation_score, BRA_reputation_calculated_at) VALUES (1, 'Netflix', 78.50, NOW());

-- Brand Uber
INSERT INTO Brand (BWS_id, BRA_name, BRA_reputation_score, BRA_reputation_calculated_at) VALUES (2, 'Uber', 71.20, NOW());

-- Keywords inclusión Netflix
INSERT INTO KeywordRule (BRA_id, KWR_keyword, KWR_match_type) VALUES
                                                                  (1, 'Netflix', 'PARTIAL'),
                                                                  (1, 'streaming', 'PARTIAL'),
                                                                  (1, 'serie', 'PARTIAL'),
                                                                  (1, 'película', 'PARTIAL'),
                                                                  (1, 'suscripción', 'PARTIAL');

-- Keywords inclusión Uber
INSERT INTO KeywordRule (BRA_id, KWR_keyword, KWR_match_type) VALUES
                                                                  (2, 'Uber', 'PARTIAL'),
                                                                  (2, 'conductor', 'PARTIAL'),
                                                                  (2, 'viaje', 'PARTIAL'),
                                                                  (2, 'tarifa', 'PARTIAL'),
                                                                  (2, 'UberEats', 'PARTIAL');

-- Keywords exclusión Netflix
INSERT INTO WorkspaceExclusionKeyword (BWS_id, WEK_keyword) VALUES
                                                                (1, 'trabajo'),
                                                                (1, 'empleo'),
                                                                (1, 'vacante'),
                                                                (1, 'oferta laboral');

-- Keywords exclusión Uber
INSERT INTO WorkspaceExclusionKeyword (BWS_id, WEK_keyword) VALUES
                                                                (2, 'trabajo'),
                                                                (2, 'empleo'),
                                                                (2, 'conductor busca trabajo'),
                                                                (2, 'vacante');

-- Canales analytics Netflix (FREE - solo YouTube activo)
INSERT INTO WorkspaceAnalyticsChannel (BWS_id, WAC2_channel, WAC2_is_active) VALUES
                                                                                 (1, 'YOUTUBE', 1),
                                                                                 (1, 'FACEBOOK', 0),
                                                                                 (1, 'TWITTER', 0),
                                                                                 (1, 'TIKTOK', 0),
                                                                                 (1, 'INSTAGRAM', 0),
                                                                                 (1, 'GOOGLE_NEWS', 0),
                                                                                 (1, 'REDDIT', 0),
                                                                                 (1, 'BLOGS', 0);

-- Canales analytics Uber (FREE - solo YouTube activo)
INSERT INTO WorkspaceAnalyticsChannel (BWS_id, WAC2_channel, WAC2_is_active) VALUES
                                                                                 (2, 'YOUTUBE', 1),
                                                                                 (2, 'FACEBOOK', 0),
                                                                                 (2, 'TWITTER', 0),
                                                                                 (2, 'TIKTOK', 0),
                                                                                 (2, 'INSTAGRAM', 0),
                                                                                 (2, 'GOOGLE_NEWS', 0),
                                                                                 (2, 'REDDIT', 0),
                                                                                 (2, 'BLOGS', 0);

-- Incidentes de prueba: SOLO data de maqueta para poblar la UI.
-- No se marcan como RESOLVED ni se les inventa resolución/asignación,
-- porque aún no existe el pipeline real (SocialVault -> Mention ->
-- MentionStream -> MonitoringRule) que detectaría y cerraría un incidente
-- de verdad. Cuando ese pipeline esté conectado, estos incidentes deberían
-- nacer solos a partir de datos reales, no insertarse a mano.
INSERT INTO ReputationIncident (BRA_id, RIN_severity_level, RIN_severity_label, RIN_title, RIN_description, RIN_status, RIN_impact_score)
VALUES (1, 2, 'MEDIO',
        'Quejas por catálogo limitado en Perú',
        'Aumento de comentarios negativos relacionados a la disponibilidad de contenido regional en Netflix Perú durante la última semana.',
        'ACTIVE', 35.00);

INSERT INTO ReputationIncident (BRA_id, RIN_severity_level, RIN_severity_label, RIN_title, RIN_description, RIN_status, RIN_impact_score)
VALUES (2, 3, 'ALTO',
        'Pico de quejas por demoras en delivery',
        'Incremento sostenido de menciones negativas sobre tiempos de espera excesivos en pedidos de Uber Eats en Lima.',
        'ACTIVE', 72.50);



