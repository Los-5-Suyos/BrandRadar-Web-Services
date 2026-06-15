Lo que falta completar por integrante siguiendo el patrón DDD del proyecto.


## Patrón a seguir

```
BoundedContext/
├── domain/model/aggregates/     ← Entidad pura sin anotaciones
├── domain/model/repositories/   ← Interfaz del repositorio
├── application/commands/        ← Comandos (escritura)
├── application/queries/         ← Queries (lectura)
├── application/commandservices/ ← Interfaz del servicio
├── application/queryservices/   ← Interfaz del servicio
├── application/internal/        ← Implementaciones
├── infrastructure/persistence/  ← JPA Entity + Mapper + Adapter
└── interfaces/rest/             ← Resources + Controller
```

---

## ✅ Ya completado (Brianna)

- T-01 → Config inicial Spring Boot + estructura DDD completa (6 Bounded Contexts)
- T-02 → Entidad UserAccount
- T-03 → POST /auth/register con BCrypt
- T-17 → CRUD /workspaces/{id}/channels
- T-20 → Documentación OpenAPI con Bearer JWT en Swagger
- T-30 → CrisisResponseEngineService vía Groq API (llama-3.3-70b-versatile)
- T-32 → DashboardRefreshScheduler cada 5 minutos con @EnableScheduling

---

## 🔲 Jean Franco — 8 tareas

### T-04 — POST /auth/login → JWT + bloqueo por intentos fallidos
- Bloqueo tras 5 intentos fallidos
- Solo cuentas con status `ACTIVE`
- Devuelve access token + refresh token
- Registrar cada intento en tabla `LoginAttempt`

### T-05 — JwtTokenProvider + SecurityConfig stateless
- Algoritmo HS256
- JwtAuthenticationFilter
- Rutas públicas: `/api/v1/auth/**`, `/swagger-ui/**`, `/api-docs/**`
- Actualizar `SecurityConfig.java` existente en `shared/infrastructure/security/`

### T-06 — GET /auth/verify?token → activa cuenta
- Token expirado → 410 Gone
- Token ya usado → 409 Conflict
- Token inválido → 400 Bad Request
- Al verificar → cambiar status a `ACTIVE`

### T-18 — WorkspaceAuthorizationFilter + auditoría
- Validar ownership por `workspaceId` en el path
- Registrar 403 en tabla de auditoría

### T-19 — GlobalExceptionHandler con @ControllerAdvice
- Mapear excepciones a 400 / 403 / 404 / 410
- Formato estándar: `{ timestamp, status, code, message }`

### T-25 — MockMentionProvider + MentionIngestionService
- Generar menciones simuladas: 32% NEG / 48% NEU / 20% POS
- Fuentes: TWITTER, INSTAGRAM, FACEBOOK, NEWS, REDDIT, TIKTOK
- Conectar con `DashboardRefreshScheduler` ya existente

### T-26 — SentimentScoreCalculator
- Score 0–100 ponderado por fuente:
    - NEWS=1.5, FACEBOOK=1.3, TWITTER=1.2, YOUTUBE=1.1, TIKTOK=1.0, REDDIT=0.9

### T-40 — QA: pruebas unitarias SentimentScoreCalculator
- 4 casos: vacía → ROJO, todas NEG → <20, todas POS → VERDE, 32/48/20 → AMBER 55–67

---

## 🔲 Joaquin — 6 tareas

### T-07 — POST /auth/forgot-password
- Token de 15 min
- Respuesta 200 siempre (protege enumeración de cuentas)
- Guardar en tabla `PasswordRecovery`

### T-08 — POST /auth/reset-password
- Completar reset con token válido
- Invalidar todas las `AuthSession` activas del usuario

### T-09 — POST /auth/refresh → rota refresh token
- Detectar invalidación por reset de contraseña
- Generar nuevo par access + refresh token

### T-21 — CORS + Flyway en producción + Railway
- Variables de entorno: `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`, `GROQ_API_KEY`
- Configurar Flyway para producción
- Desplegar en Railway con MySQL

### T-27 — DailyMetricsService — snapshot diario
- Distribución NEG/NEU/POS
- Variación vs día anterior
- Top source + sentiment index
- Upsert por workspace + fecha
- Conectar con `DashboardRefreshScheduler` ya existente

### T-31 — DashboardController + DashboardAggregatorService
- `GET /api/v1/workspaces/{id}/dashboard`
- Orquesta: snapshot, incidentes, evolución 15 días y crisis

---

## 🔲 Victor — 6 tareas

### T-10 — Entidad BrandWorkspace con Flyway V2
- Estados: `ACTIVE` / `INACTIVE`
- FK a `UserAccount`
- `BrandWorkspaceRepository`

### T-11 — POST /workspaces → crea workspace
- Validar límite por plan (FREE=1, PRO=3, ENTERPRISE=ilimitado)
- Solo usuarios con status `ACTIVE`
- Retorna 201

### T-12 — GET /workspaces y GET /workspaces/{id}
- Lista solo workspaces del usuario autenticado
- 403 si pertenece a otro usuario

### T-13 — PUT /workspaces/{id}
- Solo workspaces `ACTIVE`
- Registrar timestamp de modificación

### T-23 — Entidad Mention con Flyway V5
- Índices en `(workspaceId, publishedAt)`
- Índice en `(workspaceId, sentimentLabel, publishedAt)`

### T-24 — Entidad DailyMetricSnapshot con Flyway V6
- Cache de métricas diarias
- Constraint UNIQUE `(workspaceId, snapshotDate)`

---

## 🔲 Luis — 7 tareas

### T-14 — DELETE /workspaces/{id} → soft delete
- Transicionar a status `INACTIVE`
- Conservar historial de incidentes y menciones
- Emitir evento `WorkspaceDeactivated`

### T-15 — Entidades MonitoringRule y MonitoringChannel
- Keywords versionadas
- `ChannelType` enum: TWITTER, INSTAGRAM, FACEBOOK, NEWS, REDDIT, TIKTOK
- Repositorios correspondientes

### T-16 — CRUD /workspaces/{id}/rules
- POST crea regla con versión 1
- PUT incrementa versión + diff de keywords añadidas/eliminadas
- Emitir evento `MonitoringRuleUpdated`

### T-22 — QA: pruebas JUnit 5 + Mockito + @SpringBootTest
- Flujo completo: registro → verificación → login → workspace → reglas → desactivar → 403

### T-28 — Entidad Incident con Flyway V7
- Campos: severidad, status, `requiresImmediateAction`, `resolutionPercent`
- Deduplicación por título + status

### T-29 — IncidentDetectionService — 3 reglas de detección
- Keyword spike ≥50 → MEDIO
- Volume surge ≥100 NEG → ALTO
- 3 días >60% NEG → ALTO + acción inmediata
- Conectar con `DashboardRefreshScheduler` ya existente

### T-40 — QA: pruebas IncidentDetectionService + integración dashboard
- Reglas 2 y 3
- Deduplicación
- `@SpringBootTest` H2: flujo completo → GET /dashboard 200

---

## 📌 Notas importantes

- El `DashboardRefreshScheduler` ya está implementado en `shared/infrastructure/scheduling/` — T-25, T-27 y T-29 deben conectarse ahí
- El `SecurityConfig` está en `shared/infrastructure/security/` — T-05 debe actualizarlo para JWT
- La API key de Groq va como variable de entorno `GROQ_API_KEY` — no hardcodear en el código
- Usar el mismo patrón DDD: Domain → Application → Infrastructure → Interfaces
- Referirse al proyecto base ya implementado como guía de estructura