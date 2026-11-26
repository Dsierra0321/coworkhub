# CoWorkHub

Aplicación web individual de gestión de espacios de coworking. Permite administrar sedes, salas, equipamiento, servicios adicionales, planes, miembros y reservas, consultar disponibilidad y consumo mensual y aplicar reglas de cancelación. El frontend consume la API REST mediante Fetch y JWT.

**Autor:** Daniel Sierra.

## Tecnologías y requisitos

- HTML, CSS y JavaScript (sin dependencias de Node.js).
- Spring Boot 3.3.5, Spring Data JPA, Spring Security, JWT, Maven y Swagger UI.
- MySQL 8.4 mediante Docker Desktop o instalación local.
- JDK 21 recomendado para ejecutar la entrega; el `pom.xml` recibido aún tiene objetivo Java 17. La actualización a Spring Boot 4.1.x y objetivo Java 21 queda pendiente de comprobar según el enunciado.
- Git e IntelliJ IDEA (opcional).

## Instalación y base de datos

Clonar el repositorio y abrir la carpeta que contiene `pom.xml`.

### Docker Desktop

```powershell
docker compose --profile mysql up -d
docker ps
docker logs --tail 30 coworkhub-mysql
```

Si el contenedor ya existe, usar `docker start coworkhub-mysql` y verificar que publica el puerto esperado. No eliminar volúmenes para solucionar un conflicto de nombre.

### MySQL instalado localmente

Ejecutar en MySQL con un usuario administrador, en una instancia de desarrollo:

```sql
CREATE DATABASE IF NOT EXISTS coworkhub;
CREATE USER IF NOT EXISTS 'coworkhub'@'localhost' IDENTIFIED BY 'coworkhub';
GRANT ALL PRIVILEGES ON coworkhub.* TO 'coworkhub'@'localhost';
```

## Configuración

En `src/main/resources/application.properties`, establecer `spring.profiles.active=mysql`. La configuración de conexión está en `application-mysql.properties`. Se puede sobrescribir con variables de entorno (ejemplo PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE='mysql'
$env:COWORKHUB_DB_HOST='localhost'
$env:COWORKHUB_DB_PORT='3306'
$env:COWORKHUB_DB_NOMBRE='coworkhub'
$env:COWORKHUB_DB_USUARIO='coworkhub'
$env:COWORKHUB_DB_CONTRASENA='coworkhub'
```

La clave JWT educativa puede sobrescribirse mediante `COWORKHUB_SEGURIDAD_JWT_CLAVE`; en producción debe ser un secreto fuerte, no publicado. Las credenciales de ejemplo son únicamente para desarrollo.

## Ejecución

```powershell
mvn spring-boot:run
```

También se puede ejecutar `com.coworkhub.Main` en IntelliJ. El frontend está en `src/main/resources/static` y se sirve en [http://localhost:8080](http://localhost:8080); no requiere Live Server. El backend corre en el mismo origen. Swagger: [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html).

## Roles y usuarios de prueba

| Usuario | Contraseña | Rol | Permisos |
|---|---|---|---|
| admin | admin123 | ADMIN | Gestión de catálogos, miembros y reservas |
| recepcion | recep123 | RECEPCION | Gestionar miembros y reservas, leer catálogos |
| ana | ana123 | MIEMBRO | Consultar catálogos y gestionar sus propios datos y reservas |
| luis | luis123 | MIEMBRO | Igual que Ana, con otro plan |

## Datos iniciales

La versión probada carga catálogos y miembros con `data.sql` y genera reservas de ejemplo con `CargadorReservasDemo.java`. Los scripts alternativos `import.sql` y `migration.sql` contienen los mismos catálogos, usuarios BCrypt y miembros; no se deben ejecutar juntos. Ver `docs/GUIA-ENTREGA.md` antes de incorporar `import.sql`.

Con `ddl-auto=create`, cada arranque elimina las tablas y recrea los datos. Usar exclusivamente una base desechable para las pruebas.

## Pruebas y documentación API

Importar `docs/coworkhub.postman_collection.json` en Postman. Los 14 escenarios propuestos se basan en el código actual; ejecutar en orden con reloj fijo `2030-06-03T08:00:00` y una base recién inicializada. Restaurar el reloj a vacío después de probar. La tabla oficial de escenarios no estaba disponible para contrastarlos.

Exportar el OpenAPI real desde el servidor ejecutándose:

```powershell
New-Item -ItemType Directory -Force docs | Out-Null
Invoke-WebRequest -UseBasicParsing http://localhost:8080/v3/api-docs -OutFile docs/openapi.json
```

Para probar desde Swagger, ejecutar `POST /auth/login`, copiar `token` y pegarlo en **Authorize**. Consultar `docs/analisis.md` para el modelo y reglas de negocio.

## Evidencias de funcionamiento

Pendientes de incorporar capturas reales en `docs/evidencias/`: login, dashboard, reservas, disponibilidad, consumo, permisos, Swagger y resultados de Postman. No se declaran pruebas aprobadas hasta ejecutarlas.
