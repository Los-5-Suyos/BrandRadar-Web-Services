-- V1 already created BrandWorkspace with an earlier shape (BWS_plan, BWS_policy_* columns,
-- BWS_status as WITHOUT_CONFIGURATION/MONITORING_ACTIVE/INACTIVE). This migration evolves the
-- table to the T-10 spec: adds BWS_description, simplifies status to ACTIVO/INACTIVO and drops
-- the plan/policy columns that are no longer part of the BrandWorkspace aggregate.

UPDATE BrandWorkspace SET BWS_status = 'INACTIVO' WHERE BWS_status = 'INACTIVE';
UPDATE BrandWorkspace SET BWS_status = 'ACTIVO' WHERE BWS_status IN ('WITHOUT_CONFIGURATION', 'MONITORING_ACTIVE');

ALTER TABLE BrandWorkspace
    ADD COLUMN BWS_description TEXT AFTER BWS_name,
    MODIFY COLUMN BWS_status ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    DROP COLUMN BWS_plan,
    DROP COLUMN BWS_policy_max_brands,
    DROP COLUMN BWS_policy_alert_quota;
