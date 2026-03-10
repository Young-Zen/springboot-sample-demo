# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 2.3.12 demo application (Java 8, Maven) showcasing enterprise patterns including dual-datasource architecture (MySQL + ClickHouse), RabbitMQ messaging, Redis caching, gRPC services, and Spring Security with XSS/CSRF protection.

## Common Commands

### Build and Run
- **Full build**: `./mvnw clean package` (or `mvnw.cmd` on Windows)
- **Run application**: `./mvnw spring-boot:run`
- **Run tests**: `./mvnw test`
- **Run specific test**: `./mvnw test -Dtest=ClassName#methodName`
- **Tests with -parameters flag**: `./mvnw test -Punit-test` (required when using @SpyBean with @Cacheable methods)

### Code Quality
- **Format code**: `./mvnw spotless:apply` (auto-applies during compile phase)
- **Check format**: `./mvnw spotless:check`
- **Run PMD analysis**: `./mvnw pmd:pmd`
- **Run PMD check**: `./mvnw pmd:check` (Ali P3C rules, enforced in verify phase)

### Docker
- **Build Docker image**: `./mvnw docker:build` (uses Spotify docker-maven-plugin)
- **Build with Dockerfile**: `./mvnw dockerfile:build`

### Database
- **Flyway migrations**: Auto-applied on startup via `spring.flyway.enabled=true`
- **Migration locations**: `classpath:db/migration/mysql` and `classpath:db/migration/clickhouse`
- **Flyway metadata table**: `t_flyway_schema_history` in `demo` schema

### gRPC
- **Proto compilation**: `./mvnw compile` (protobuf-maven-plugin runs automatically)
- **Generated sources**: `target/generated-sources/protobuf/`
- **gRPC server port**: 28080 (configurable via `grpc.server.port`)

## Architecture

### Package Structure
```
com.sz.springbootsample.demo/
├── annotation/           # Custom annotations (@Sensitive, @IgnoreTracing, @CustomParam)
├── aspect/              # AOP aspects (LogAspect with Pointcuts)
├── config/
│   ├── datasource/      # Multi-datasource config (MySQL, ClickHouse)
│   ├── grpc/           # gRPC server setup
│   ├── message/        # RabbitMQ configuration
│   ├── mybatisplus/    # MyBatis-Plus config with CryptoJacksonTypeHandler
│   ├── property/       # Custom properties (GrpcProperties, LogProperties)
│   ├── redis/          # Redis config with Lettuce
│   ├── security/       # Security config, XSS/CSRF protection
│   ├── swagger/        # Swagger2 configuration
│   └── webmvc/         # Web MVC config, interceptors, response advice
├── controller/         # REST controllers
├── dao/                # Data access objects
├── dto/                # Data transfer objects (ResponseResultDTO)
├── entity/             # JPA/MyBatis entities
├── enums/              # Enumerations
├── exception/          # Global exception handler
├── form/               # Request form objects
├── mapper/             # MyBatis mappers
├── serializer/         # JSON serializers (SensitiveJsonSerializer)
├── service/            # Business logic
│   └── rabbitmq/      # RabbitMQ receivers
├── thread/             # Thread pools and ThreadLocal utilities
├── util/               # Utility classes
└── vo/                 # View objects
```

### Key Architectural Patterns

**Dual Datasource Architecture**
- MySQL: Primary transactional database (`MysqlDataSourceConfig`)
- ClickHouse: Analytical database (`ClickHouseDataSourceConfig`)
- Separated SqlSessionFactory configurations
- Flyway only manages MySQL migrations

**Response Envelope Pattern**
- `DefaultResponseBodyAdvice`: Adds tracing headers (PointcutCount, LogCode, LogStep) to all responses
- Uses `LogHolder` (ThreadLocal) for request-scoped trace data

**Security & XSS Protection**
- `XssHttpServletRequestWrapper`/`XssHttpServletResponseWrapper`: XSS filtering
- `DefaultWebSecurityConfigurerAdapter`: Custom security rules
- CSRF protection enabled via `CsrfRequireMatcher`

**Sensitive Data Handling**
- `@Sensitive` annotation with `SensitiveJsonSerializer`: Deserializes fields
- Custom `PASS()` prefix for encrypted properties (decrypted via `DecryptedPropertyEnvironmentPostProcessor`)
- `CryptoJacksonTypeHandler`: MyBatis-Plus type handler for field encryption

**AOP Logging**
- `LogAspect`: Intercepts methods defined in `Pointcuts`
- `@IgnoreTracing`: Exclude methods from tracing
- Response headers populated via ThreadLocal `LogHolder`

**gRPC Integration**
- Proto files: `src/main/proto/example/stock/stock_quote.proto`
- Auto-generated gRPC Java classes during compile
- Server runs on separate port (28080)

### Property Decryption
Sensitive properties use `PASS(base64_encoded_value)` format:
- Decrypted at startup by `DecryptedPropertyEnvironmentPostProcessor`
- Keys configured via `custom.decrypt.property.key`
- Affects: MySQL password, ClickHouse password, Redis password, RabbitMQ password

### Configuration Profiles
- **test**: Swagger CSRF enabled (`custom.swagger.csrf.enable: true`)
- **prod**: Swagger disabled (`custom.swagger.enable: false`)
- Default active: `test`

### Dependencies to Note
- **MapStruct 1.5.5**: Bean mapping (annotation processor configured)
- **Lombok**: Code generation
- **MyBatis-Plus 3.5.7**: ORM with code generator (test scope)
- **Hutool 5.8.29**: Chinese utility library
- **Guava 32.1.3-jre**: Google core libraries
- **OkHttp 3.14.9**: HTTP client
- **Alibaba TransmittableThreadLocal 2.14.5**: Thread context propagation

## Testing Notes
- Minimum 25% code coverage enforced by JaCoCo
- Mockito 3.12.4 with inline mock maker
- gRPC example code excluded from coverage
- Mapper implementations excluded from PMD checks

## Monitoring
- Actuator endpoints: `/health`, `/info`, `/prometheus`, `/loggers`
- Spring Boot Admin client configured
- Application context path: `/api/demo`
- Default server port: 8080
