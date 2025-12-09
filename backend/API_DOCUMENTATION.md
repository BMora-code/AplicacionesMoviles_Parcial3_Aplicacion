# Backend Android - Documentación API

## Configuración

### Requisitos
- Java 21
- MongoDB (ejecutándose en localhost:27017)
- Maven

### Instalación
```bash
mvn clean install
mvn spring-boot:run
```

---

## ENDPOINTS API

### 1. AUTENTICACIÓN

#### POST /api/auth/register
Registra un nuevo usuario.

**Request:**
```json
{
  "email": "usuario@example.com",
  "password": "contraseña123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": "uuid",
    "email": "usuario@example.com",
    "characterVariant": 1,
    "profileImageUrl": null
  }
}
```

---

#### POST /api/auth/login
Inicia sesión de usuario.

**Request:**
```json
{
  "email": "usuario@example.com",
  "password": "contraseña123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": "uuid",
    "email": "usuario@example.com",
    "characterVariant": 1,
    "profileImageUrl": null
  }
}
```

---

### 2. USUARIOS

#### GET /api/users/me
Obtiene el perfil del usuario autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "id": "uuid",
  "email": "usuario@example.com",
  "characterVariant": 1,
  "profileImageUrl": "/api/files/uuid_filename.jpg"
}
```

---

#### PATCH /api/users/me
Actualiza la variante de personaje del usuario.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "characterVariant": 2
}
```

**Response:**
```json
{
  "id": "uuid",
  "email": "usuario@example.com",
  "characterVariant": 2,
  "profileImageUrl": "/api/files/uuid_filename.jpg"
}
```

---

#### POST /api/users/me/avatar
Sube la imagen de avatar del usuario.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

**Request:**
- Form parameter: `file` (archivo de imagen)

**Response:**
```json
{
  "id": "uuid",
  "email": "usuario@example.com",
  "characterVariant": 1,
  "profileImageUrl": "/api/files/uuid_filename.jpg"
}
```

---

### 3. UBICACIÓN

#### POST /api/users/me/location
Guarda la ubicación del usuario (latitud y longitud).

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "latitude": 40.4168,
  "longitude": -3.7038
}
```

**Response:**
```json
{
  "id": "uuid",
  "userId": "uuid",
  "latitude": 40.4168,
  "longitude": -3.7038,
  "timestamp": "2025-12-07T15:30:00"
}
```

---

#### GET /api/users/me/location
Obtiene todas las ubicaciones registradas del usuario.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "id": "uuid",
    "userId": "uuid",
    "latitude": 40.4168,
    "longitude": -3.7038,
    "timestamp": "2025-12-07T15:30:00"
  },
  {
    "id": "uuid2",
    "userId": "uuid",
    "latitude": 41.3874,
    "longitude": 2.1686,
    "timestamp": "2025-12-07T16:00:00"
  }
]
```

---

## ESTRUCTURA DEL PROYECTO

```
src/main/java/com/app/backend/
├── BackendApplication.java          # Clase principal
├── config/
│   ├── SecurityConfig.java          # Configuración de seguridad
│   └── WebConfig.java               # Configuración CORS
├── controller/
│   ├── AuthController.java          # Endpoints de autenticación
│   ├── UserController.java          # Endpoints de usuarios
│   ├── LocationController.java      # Endpoints de ubicación
│   └── FileController.java          # Endpoints de archivos
├── service/
│   ├── AuthService.java             # Lógica de autenticación
│   ├── UserService.java             # Lógica de usuarios
│   └── LocationService.java         # Lógica de ubicación
├── repository/
│   ├── UserRepository.java          # Acceso a datos de usuarios
│   └── LocationLogRepository.java   # Acceso a datos de ubicación
├── model/
│   ├── User.java                    # Entidad Usuario
│   └── LocationLog.java             # Entidad Ubicación
├── security/
│   ├── JwtTokenProvider.java        # Generador y validador JWT
│   └── JwtAuthenticationFilter.java # Filtro de autenticación
├── dto/
│   ├── AuthResponse.java            # Respuesta de autenticación
│   ├── UserDto.java                 # DTO de usuario
│   ├── LoginRequest.java            # Request de login
│   ├── RegisterRequest.java         # Request de registro
│   ├── UpdateCharacterRequest.java  # Request de actualización
│   └── LocationRequest.java         # Request de ubicación
└── exception/
    ├── GlobalExceptionHandler.java  # Manejador global de excepciones
    └── ErrorResponse.java           # Respuesta de error

src/main/resources/
└── application.properties            # Configuración de la aplicación
```

---

## CARACTERÍSTICAS IMPLEMENTADAS

✅ **Autenticación JWT**
- Generación de tokens en login y registro
- Validación de tokens en todos los endpoints protegidos

✅ **CORS Habilitado**
- Permitir solicitudes desde cualquier origen
- Métodos: GET, POST, PUT, PATCH, DELETE, OPTIONS

✅ **Manejo de Imágenes**
- Guardar archivos en carpeta local `uploads/avatars/`
- Generar URL para acceder a las imágenes
- Endpoint para descargar imágenes

✅ **MongoDB**
- Integración completa con MongoRepository
- Documentos bien estructurados
- Índices para búsquedas por email

✅ **Validaciones**
- Email válido y único
- Campos requeridos
- Validación de contraseña

✅ **Respuestas JSON**
- Respuestas claras y estructuradas
- Manejo centralizado de excepciones
- Mensajes de error descriptivos

---

## EJEMPLOS DE USO CON CURL

### 1. Registrar usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "123456"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "123456"
  }'
```

### 3. Obtener perfil
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer {token}"
```

### 4. Actualizar personaje
```bash
curl -X PATCH http://localhost:8080/api/users/me \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "characterVariant": 2
  }'
```

### 5. Subir avatar
```bash
curl -X POST http://localhost:8080/api/users/me/avatar \
  -H "Authorization: Bearer {token}" \
  -F "file=@/ruta/a/imagen.jpg"
```

### 6. Guardar ubicación
```bash
curl -X POST http://localhost:8080/api/users/me/location \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 40.4168,
    "longitude": -3.7038
  }'
```

---

## NOTAS IMPORTANTES

1. **Cambiar la clave secreta JWT** en `application.properties`:
   ```properties
   app.jwt.secret=TuClaveSecretaMuySegura
   ```

2. **Configurar MongoDB**: Asegúrate de que MongoDB esté corriendo en `localhost:27017`

3. **Crear directorio de uploads**: El directorio `uploads/avatars/` se crea automáticamente

4. **Token expiration**: Por defecto, los tokens expiran en 24 horas (86400000ms)

---

## TECNOLOGÍAS UTILIZADAS

- **Spring Boot 3.4.12**
- **Java 21**
- **MongoDB**
- **Spring Security**
- **JWT (jjwt 0.12.3)**
- **Lombok**
- **Maven**

---
