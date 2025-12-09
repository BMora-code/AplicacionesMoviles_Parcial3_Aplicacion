# Backend Android - Spring Boot + MongoDB + JWT

Backend completo para aplicación Android con autenticación JWT, gestión de usuarios, ubicación y carga de avatares.

## 🚀 Inicio Rápido

### Requisitos previos
- Java 21 instalado
- MongoDB instalado y ejecutándose en `localhost:27017`
- Maven 3.8+

### Pasos de configuración

1. **Clonar/Descargar el proyecto**
```bash
cd backend
```

2. **Compilar el proyecto**
```bash
mvn clean install
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

---

## 📋 Modelos de datos

### User
```javascript
{
  id: UUID,
  email: string (único),
  passwordHash: string (bcrypt),
  characterVariant: int,
  profileImageUrl: string,
  createdAt: datetime
}
```

### LocationLog
```javascript
{
  id: UUID,
  userId: UUID,
  latitude: double,
  longitude: double,
  timestamp: datetime
}
```

---

## 🔐 Autenticación con JWT

Todos los endpoints protegidos requieren un header:
```
Authorization: Bearer {token}
```

**Flujo:**
1. Usuario se registra o inicia sesión en `/api/auth/register` o `/api/auth/login`
2. Recibe un token JWT válido por 24 horas
3. Usa el token en todas las solicitudes protegidas

---

## 📡 Endpoints principales

### AUTH
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión

### USERS
- `GET /api/users/me` - Obtener perfil autenticado
- `PATCH /api/users/me` - Actualizar variante de personaje
- `POST /api/users/me/avatar` - Subir imagen de perfil

### LOCATION
- `POST /api/users/me/location` - Guardar ubicación
- `GET /api/users/me/location` - Obtener historial de ubicaciones

---

## 📁 Estructura del proyecto

```
src/main/
├── java/com/app/backend/
│   ├── BackendApplication.java       # Punto de entrada
│   ├── config/                       # Configuración (Seguridad, CORS)
│   ├── controller/                   # Controladores REST
│   ├── service/                      # Lógica de negocio
│   ├── repository/                   # Acceso a datos MongoDB
│   ├── model/                        # Entidades
│   ├── security/                     # JWT y seguridad
│   ├── dto/                          # Data Transfer Objects
│   └── exception/                    # Manejo de errores
└── resources/
    └── application.properties        # Configuración de la app
```

---

## ⚙️ Configuración

Editar `src/main/resources/application.properties`:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/android_app

# JWT - CAMBIAR EN PRODUCCIÓN
app.jwt.secret=TuClaveSecretaMasSegura
app.jwt.expiration=86400000  # 24 horas en milisegundos

# Server
server.port=8080

# Upload
spring.servlet.multipart.max-file-size=10MB
```

---

## 💾 Base de datos MongoDB

Crear la base de datos (opcional, se crea automáticamente):
```javascript
use android_app;
db.users.createIndex({ "email": 1 }, { unique: true });
```

---

## 📝 Ejemplo de flujo completo

```bash
# 1. Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'

# Respuesta:
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "user": {"id":"uuid","email":"test@example.com",...}
# }

# 2. Obtener perfil (usar el token recibido)
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

# 3. Subir avatar
curl -X POST http://localhost:8080/api/users/me/avatar \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -F "file=@/ruta/imagen.jpg"

# 4. Guardar ubicación
curl -X POST http://localhost:8080/api/users/me/location \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{"latitude":40.4168,"longitude":-3.7038}'
```

---

## 🔧 Solución de problemas

### "No MongoDB available on localhost:27017"
- Asegúrate de que MongoDB esté ejecutándose
- Windows: `mongod` en terminal
- Docker: `docker run -d -p 27017:27017 mongo`

### "Conexión rechazada en puerto 8080"
- El puerto 8080 ya está en uso
- Cambiar puerto en `application.properties`: `server.port=8081`

### "Email ya registrado"
- Verificar base de datos MongoDB o usar otro email

---

## 📦 Dependencias principales

- **Spring Boot 3.4.12** - Framework principal
- **Spring Security** - Autenticación y autorización
- **jjwt 0.12.3** - Generación y validación JWT
- **Spring Data MongoDB** - ORM para MongoDB
- **Lombok** - Reducir boilerplate

---

## 🌐 CORS

CORS está habilitado para todas las rutas `/api/**`:
- Orígenes: Todos (`*`)
- Métodos: GET, POST, PUT, PATCH, DELETE
- Headers: Todos permitidos
- Credenciales: No requeridas

---

## 📚 Documentación adicional

Ver `API_DOCUMENTATION.md` para documentación completa de endpoints.

---

## 📄 Licencia

Proyecto de ejemplo para aplicación Android.

---

## 👨‍💻 Desarrollo

Para cambios en desarrollo:
```bash
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

---
