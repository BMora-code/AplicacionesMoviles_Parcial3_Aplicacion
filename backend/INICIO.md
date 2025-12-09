# 🚀 BACKEND ANDROID - SPRING BOOT + MONGODB + JWT

## ✅ STATUS: 100% COMPLETADO Y LISTO PARA USAR

---

## 📌 COMIENZA AQUÍ

### Opción 1: Ejecución Local (Recomendado para desarrollo)

```bash
# 1. Asegúrate de que MongoDB esté corriendo
mongod

# 2. En otra terminal, compila y ejecuta
mvn clean install
mvn spring-boot:run

# 3. La app estará en http://localhost:8080
```

### Opción 2: Ejecución con Docker

```bash
# Asegúrate de tener Docker instalado
docker-compose up

# La app estará en http://localhost:8080
```

---

## 🧭 DOCUMENTACIÓN RÁPIDA

| Archivo | Contenido |
|---------|-----------|
| **QUICK_REFERENCE.txt** | 📌 Referencia rápida (LEER PRIMERO) |
| **README.md** | 📖 Guía de inicio rápido |
| **API_DOCUMENTATION.md** | 📚 Documentación completa de endpoints |
| **HELP.md** | 🔧 Guía de desarrollo y solución de problemas |
| **IMPLEMENTACION_COMPLETADA.md** | ✨ Resumen de implementación |

---

## 🎯 ENDPOINTS PRINCIPALES

### Autenticación (Sin token)
```bash
POST   /api/auth/register    # Registrar usuario
POST   /api/auth/login       # Iniciar sesión
```

### Usuarios (Con token)
```bash
GET    /api/users/me         # Obtener perfil
PATCH  /api/users/me         # Actualizar personaje
POST   /api/users/me/avatar  # Subir avatar
```

### Ubicación (Con token)
```bash
POST   /api/users/me/location    # Guardar ubicación
GET    /api/users/me/location    # Obtener historial
```

---

## 🧪 TEST RÁPIDO CON CURL

```bash
# 1. Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'

# Respuesta:
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "user": {"id":"...","email":"test@example.com",...}
# }

# 2. Usar el token en requests protegidos
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer {TOKEN}"
```

---

## 📁 ESTRUCTURA DEL CÓDIGO

```
src/main/java/com/app/backend/
├── controller/              ← Endpoints REST
├── service/                 ← Lógica de negocio
├── repository/              ← Acceso a datos MongoDB
├── model/                   ← Entidades
├── config/                  ← Configuración
├── security/                ← JWT y autenticación
├── dto/                     ← Objetos de transferencia
└── exception/               ← Manejo de errores
```

---

## 🔐 CARACTERÍSTICAS DE SEGURIDAD

✅ **JWT Authentication** - Tokens con expiración de 24h  
✅ **BCrypt Password Encryption** - Contraseñas seguras  
✅ **CORS Habilitado** - Comunicación con Android  
✅ **Validaciones** - Email único, campos requeridos  
✅ **Manejo de Errores** - Respuestas claras en JSON  

---

## 💾 BASE DE DATOS MONGODB

Colecciones automáticas:
- `users` - Usuarios registrados
- `location_logs` - Historial de ubicaciones

La base de datos se crea automáticamente al primer request.

---

## ⚡ PRÓXIMOS PASOS

1. **Lee QUICK_REFERENCE.txt** para una visión general
2. **Ejecuta `mvn spring-boot:run`** para iniciar
3. **Importa postman_collection.json** en Postman para probar
4. **Lee API_DOCUMENTATION.md** para detalles de endpoints
5. **Consulta HELP.md** si necesitas crear nuevos endpoints

---

## 🛠️ REQUISITOS

- ✅ Java 21 JDK
- ✅ MongoDB 7.0+
- ✅ Maven 3.8+
- ✅ Spring Boot 3.4.12

---

## 📊 RESUMEN DE IMPLEMENTACIÓN

| Componente | Status |
|-----------|--------|
| Models (User, LocationLog) | ✅ Completado |
| Repositories (MongoDB) | ✅ Completado |
| Services (Auth, User, Location) | ✅ Completado |
| Controllers (API endpoints) | ✅ Completado |
| Security & JWT | ✅ Completado |
| CORS Configuration | ✅ Completado |
| File Upload | ✅ Completado |
| Error Handling | ✅ Completado |
| Docker Support | ✅ Completado |
| Documentation | ✅ Completado |

---

## 📞 ¿PROBLEMAS?

1. **Puerto 8080 en uso** → Ver HELP.md
2. **MongoDB no conecta** → Ver HELP.md
3. **No entiendo un endpoint** → Ver API_DOCUMENTATION.md
4. **Quiero agregar funcionalidad** → Ver HELP.md (Desarrollo)

---

## 🎓 APRENDIZAJE

Este proyecto implementa:
- ✓ Autenticación con JWT en Spring Boot
- ✓ Estructura en capas (Controller → Service → Repository)
- ✓ Integración con MongoDB
- ✓ Manejo de errores centralizado
- ✓ CORS para aplicaciones móviles
- ✓ Carga de archivos (multipart)
- ✓ Docker y Docker Compose

---

## 📄 LICENCIA

Proyecto de ejemplo para aplicación Android.

---

## 🚀 ¡LISTO PARA PRODUCCIÓN!

El backend está completamente funcional y listo para:
- ✅ Conectar con tu aplicación Android
- ✅ Desplegar en servidor de producción
- ✅ Escalar para múltiples usuarios
- ✅ Agregar nuevas funcionalidades

**Comienza con: `mvn spring-boot:run`**

---

*Creado: 7 de diciembre de 2025*
*Framework: Spring Boot 3.4.12*
*Java: 21*
*Base de datos: MongoDB*
