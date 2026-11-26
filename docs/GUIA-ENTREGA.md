# Guía de integración y pruebas

## Fase 1: documentación y pruebas sin alterar Java
Copiar README.md y docs/coworkhub.postman_collection.json al proyecto existente. Conservar docs/analisis.md y el frontend corregido. Los archivos SQL se entregan por separado dentro del ZIP y NO deben copiarse todavía.

Para exportar docs/openapi.json, con Spring Boot activo:

```powershell
New-Item -ItemType Directory -Force docs | Out-Null
Invoke-WebRequest -UseBasicParsing http://localhost:8080/v3/api-docs -OutFile docs/openapi.json
(Get-Content docs/openapi.json -Raw | ConvertFrom-Json).openapi
```

## Fase 2: Postman
Usar solo una base de pruebas descartable, pues ddl-auto=create borra todas las tablas. En application.properties cambiar temporalmente `coworkhub.reloj.fijo=2030-06-03T08:00:00` y reiniciar. Importar la colección, ejecutar la carpeta de preparación primero y los escenarios 01 a 14 en orden (o ejecutar toda la colección una vez con Runner). No ejecutar repetidamente sobre el mismo conjunto de reservas. Los identificadores de catálogos y miembros se descubren mediante la API. Los tokens se almacenan como variables de colección durante la ejecución; no exportar tokens reales al repositorio. Las solicitudes de preparación no cuentan como escenarios. Revisar Test Results y guardar evidencias. Restaurar `coworkhub.reloj.fijo=` al terminar. No se ha ejecutado la colección en un servidor real en esta revisión.

## Fase 3: inicialización SQL (cambio opcional a probar por separado)
El enunciado solicita tanto import.sql como migration.sql. Se prepararon ambos con los datos estáticos originales, en sentencias de una línea, usando los nombres reales de las tablas existentes. No contienen las 15 reservas, que se generan en Java con el Clock. No son migraciones Flyway. Para migrar a import.sql en el perfil MySQL, copiar ambos archivos a src/main/resources, cambiar `spring.sql.init.mode=never` en application-mysql.properties y mantener `spring.jpa.hibernate.ddl-auto=create` únicamente en la base de pruebas. Dejar data.sql sin ejecutar; migration.sql se conserva como script manual alternativo y no debe ejecutarse adicionalmente. Hibernate puede ejecutar import.sql también con otros perfiles create/create-drop: si se usan, desactivar la inicialización Spring en esos perfiles también. Verificar que los catálogos y las 15 reservas de ejemplo se cargan correctamente. Si falla, retirar import.sql y restaurar la configuración anterior. La compatibilidad real del nuevo mecanismo todavía no se ha probado.

## Pendientes de conformidad
- Conseguir rubrica.md y las tablas oficiales 1 a 17 del enunciado (en el texto recibido solo aparecen sus nombres). Los 14 escenarios propuestos no son una transcripción de la tabla oficial.
- Revisar Java 21 y Spring Boot 4.1.x en una rama independiente; el proyecto recibido utiliza Spring Boot 3.3.5 y objetivo Java 17.
- Revisar nombres ingleses de entidades, tablas explícitas y roles exigidos; actualmente son españoles. No renombrar sin coordinar cambios de frontend, DTO, SQL y seguridad.
- Confirmar requisitos de reservas en import.sql; hoy dependen del cargador Java. Revisar pruebas de concurrencia y límites de membresía.
- Actualizar docs/analisis.md (aún describe la interfaz gráfica como fuera de alcance) y añadir evidencias reales.
