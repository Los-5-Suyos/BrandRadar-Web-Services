# Brand Radar — Backend

Backend de **Brand Radar**, una plataforma de monitoreo de reputación digital en tiempo real. Permite a marcas y agencias rastrear menciones reales sobre su negocio en redes sociales, medir el sentimiento con IA, detectar crisis de reputación automáticamente, y generar reportes ejecutivos — todo desde un solo lugar.

Proyecto universitario desarrollado con **Spring Boot** + **MySQL**, siguiendo arquitectura hexagonal / DDD por bounded contexts.

---

## Stack técnico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 26 |
| Framework | Spring Boot 4.0.7 |
| Base de datos | MySQL |
| Autenticación | JWT (access + refresh token) |
| Documentación de API | Swagger / OpenAPI (springdoc) |
| IA de análisis de sentimiento e insights | Groq (llama-3.3-70b-versatile) |
| Datos reales de redes sociales | [SociaVault API](https://sociavault.com) |
| Datos de YouTube | YouTube Data API v3 (oficial de Google) |
| Generación de reportes | Apache POI (Excel), Apache PDFBox (PDF) |

---

## Arquitectura

El backend está organizado en **bounded contexts** (DDD), cada uno con sus propias capas `domain` / `application` / `infrastructure` / `interfaces`:

```
brandradar/
├── iam/                     → Autenticación, usuarios, perfil, notificaciones
├── brandworkspace/          → Workspaces, marcas, config, canales, keywords, suscripción
├── reputationmonitoring/    → Menciones, mention streams, incidentes, reportes
├── sentimentintelligence/   → Análisis de sentimiento, snapshots diarios, insights con IA
├── crisisdetection/         → Alertas de crisis, reglas de monitoreo, preferencias de alerta
├── infrastructurehealth/    → Salud del sistema (uptime, errores técnicos) — solo ADMIN
└── shared/                  → Seguridad, dashboard agregado, configuración transversal
```

---

## Funcionalidades principales

### 🔒 Seguridad
- Autenticación JWT con access + refresh token.
- Ownership guard: cada endpoint valida que el recurso pertenezca al usuario autenticado (previene IDOR).
- Límites de plan validados en el backend (no solo en frontend).
- Ninguna API key ni secreto vive en el código fuente — todo por variables de entorno.

### 📡 Ingesta de datos reales (SociaVault)
- **YouTube, Twitter, Reddit y TikTok** conectados con datos reales (no simulados).
- Filtro de idioma (es/en) para descartar spam.
- Filtrado por keywords de inclusión/exclusión configurables por marca.
- Botón de actualización manual — el usuario controla cuándo se consumen créditos de la API, en vez de un scheduler automático descontrolado.
- Arquitectura extensible (`ChannelMentionProvider`) — agregar un canal nuevo es agregar una clase, sin tocar el resto del sistema.

<<<<<<< HEAD
- T-14 → DELETE /workspaces/{id} → soft delete
- T-15 → Entidades MonitoringRule y MonitoringChannel
- T-16 → CRUD /workspaces/{id}/rules
- T-22 → QA: pruebas JUnit 5 + Mockito + @SpringBootTest
- T-28 → Entidad Incident con Flyway V7
- T-29 → IncidentDetectionService — 3 reglas de detección
- T-40 → QA: pruebas IncidentDetectionService + integración dashboard
- T-13 → PUT /workspaces/{id}
=======
### 📊 Dashboard
- Sentiment score calculado por IA, analizado una vez por mención (no recalculado en cada carga de pantalla).
- Serie histórica de 14 días, deltas vs. día anterior.
- Score y diagnóstico por canal, con insights generados por IA (con fallback automático a reglas si la IA falla).
- Ranking de keywords críticas.
- 5 preferencias de alerta configurables (caída de score, pico de negatividad, keyword crítica, nuevo incidente, volumen inusual) con notificaciones en tiempo real.

### 💬 Menciones
- Listado con filtros server-side (sentimiento, plataforma, búsqueda de texto).
- Análisis IA por mención individual (diagnóstico + borrador de respuesta).
- Acciones: marcar como atendida, crear incidente desde una mención.
- Exportación a CSV, Excel y PDF.
- Drill-down de comentarios reales de TikTok bajo demanda.

### 🚨 Incidentes
- Modelo de dos niveles: `CrisisAlert` (la alerta/notificación automática) → genera un `ReputationIncident` (el caso con seguimiento, asignación y resolución).
- Estados: ACTIVO → MONITOREADO → RESUELTO, con porcentaje de progreso.
- Diagnóstico por IA conectado, con historial de análisis guardado.
- Ranking de keywords específico por incidente.

### 📄 Reportes
- Generación de reportes por rango de fechas, en PDF/Excel/CSV.
- Resumen ejecutivo en lenguaje natural generado por IA.
- Métricas con variación vs. periodo anterior (sentiment score, menciones, reach estimado).
- Top keywords, cuentas más críticas, evolución diaria.
- Programación de envíos automáticos (configuración persistida; el envío real de email requiere conectar un servicio SMTP, no incluido).

### 💳 Suscripción (simulada)
- Catálogo de planes (FREE / PRO / ENTERPRISE).
- Flujo de pago simulado — **nunca se procesa ni almacena un número de tarjeta o CVC real**, solo los últimos 4 dígitos y la marca de la tarjeta, siguiendo buenas prácticas de seguridad (PCI-DSS-friendly) aunque no haya cobro real de por medio.

---

## Configuración local

### 1. Variables de entorno necesarias

Este proyecto **no trae ninguna key real en el código**. Necesitas tus propias credenciales:

| Variable | Dónde conseguirla |
|---|---|
| `GROQ_API_KEY` | https://console.groq.com/keys |
| `YOUTUBE_API_KEY` | https://console.cloud.google.com/apis/credentials |
| `SOCIAVAULT_API_KEY` | https://sociavault.com |
| `JWT_SECRET` | Genera uno random, ej: `openssl rand -base64 64` |
| `SPRING_DATASOURCE_URL/USERNAME/PASSWORD` | Tu instancia local de MySQL |
| `ALLOWED_ORIGINS` | `http://localhost:4200` en desarrollo |

### 2. Cómo correrlo

**Opción recomendada (IDE):** crea `src/main/resources/application-local.properties` (está en `.gitignore`, nunca se sube) con tus valores reales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/BrandRadar_DB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=tu_password

groq.api.key=tu_key_de_groq
youtube.api.key=tu_key_de_youtube
sociavault.api.key=tu_key_de_sociavault
jwt.secret=tu_secret_generado
allowed.origins=http://localhost:4200
```

Luego, en la configuración de Run de tu IDE, activa el perfil `local` (Active profiles: `local`).

**Por terminal:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### 3. Base de datos

`spring.jpa.hibernate.ddl-auto=update` — Hibernate crea/actualiza las tablas automáticamente a partir de las entidades, no hace falta correr scripts SQL manuales salvo el seed de datos de prueba inicial.

### 4. Documentación de la API

Con el proyecto corriendo, la documentación interactiva está en:

```
http://localhost:8080/swagger-ui.html
```

---

## ⚠️ Seguridad — importante para quien clone este repo

- **Nunca** subas `application-local.properties` ni `.env` — ambos están en `.gitignore`.
- Si alguna vez ves un error de GitHub tipo *"Push cannot contain secrets"*, significa que un archivo con credenciales quedó atrapado en el historial de commits — no lo ignores. Hay que limpiar el historial (`git reset`/rama nueva desde `main`) y **rotar esa credencial de inmediato**, aunque el push haya sido bloqueado.
- Antes de hacer merge a `main`, verifica que no haya keys hardcodeadas en ningún `.properties` que sí esté trackeado por git.

---

## Estado del proyecto

Backend funcionalmente completo: seguridad, IAM, workspace/configuración, dashboard, menciones, incidentes, reportes, suscripción, e ingesta real multi-plataforma. Pendiente: conexión con el frontend (actualmente consume datos simulados).
>>>>>>> 9807b1a2c5c7cfb70bd143874949994e4eec5d8f
