## E-Commerce Backend

Este proyecto es el backend para una aplicación de comercio electrónico. Ofrece servicios RESTful para la gestión de usuarios, productos, órdenes y el carrito de compras. Está construido con **Java** utilizando el framework **Spring Boot**.

---

### **Características**

1. **Gestión de Usuarios**:
    - Registro e inicio de sesión.
    - Roles de usuario (ADMIN, USER).
    - Autenticación y autorización con JWT.
    - Actualización de dirección del usuario desde el perfil.

2. **Gestión de Productos**:
    - CRUD (Crear, Leer, Actualizar, Eliminar) para productos.
    - Validación de stock antes de procesar órdenes.
    - Listado de productos más vendidos y generación de métricas de ingresos.

3. **Carrito de Compras**:
    - Añadir, actualizar y eliminar productos.
    - Limpieza automática del carrito después de procesar una orden.

4. **Gestión de Órdenes**:
    - Creación de órdenes basadas en los productos del carrito.
    - Actualización del estado de las órdenes.
    - Validación de stock antes de confirmar una orden.

5. **Seguridad**:
    - Autenticación basada en JWT.
    - Rutas protegidas según el rol del usuario.

6. **Métricas y Análisis**:
    - Endpoints para obtener los productos más vendidos y los ingresos generados.

---

### **Requisitos**

- **Java** 17+
- **Maven** 3.8+
- **MySQL** 8.0+
- **Postman** (opcional para probar la API)

---

### **Configuración del Proyecto**

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/tu-repositorio/backend-ecommerce.git
   cd backend-ecommerce
   ```

2. **Configurar la base de datos**:
    - Crear una base de datos en MySQL llamada `ecommerce`.
    - Configura tus credenciales en el archivo `application.properties`:

      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
      spring.datasource.username=tu_usuario
      spring.datasource.password=tu_contraseña
      ```

3. **Instalar dependencias**:

   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**:

   ```bash
   mvn spring-boot:run
   ```

---

### **Estructura del Proyecto**

- **`model`**: Entidades del modelo de datos.
- **`repository`**: Interfaces para interactuar con la base de datos.
- **`service`**: Contiene la lógica de negocio.
- **`dto`**: Objetos de transferencia de datos.
- **`controller`**: Define los endpoints de la API REST.
- **`config`**: Configuraciones de seguridad y JWT.
- **`exception`**: Manejo de excepciones personalizadas.

---

### **Endpoints Principales**

| Recurso       | Método | Endpoint                    | Descripción                                      |
|---------------|--------|-----------------------------|--------------------------------------------------|
| **Auth**      | POST   | `/auth/register`            | Registra un nuevo usuario.                      |
|               | POST   | `/auth/authenticate`        | Inicia sesión y devuelve un token JWT.          |
| **Usuarios**  | GET    | `/admin/users`              | Obtiene todos los usuarios (ADMIN).             |
|               | GET    | `/users/{id}`               | Obtiene un usuario específico por ID.           |
|               | PATCH  | `/users/{id}/address`       | Actualiza la dirección de un usuario.           |
| **Productos** | GET    | `/products`                 | Lista todos los productos.                      |
|               | POST   | `/products`                 | Crea un producto (ADMIN).                       |
|               | PUT    | `/products/{id}`            | Actualiza un producto (ADMIN).                  |
|               | DELETE | `/products/{id}`            | Elimina un producto (ADMIN).                    |
|               | GET    | `/products/top-selling`     | Obtiene los productos más vendidos.             |
| **Carrito**   | GET    | `/cart`                     | Obtiene los productos en el carrito.            |
|               | POST   | `/cart/add`                 | Añade un producto al carrito.                   |
|               | DELETE | `/cart/remove/{id}`         | Elimina un producto del carrito.                |
| **Órdenes**   | GET    | `/orders/user`              | Obtiene las órdenes del usuario autenticado.    |
|               | POST   | `/orders`                   | Crea una nueva orden.                           |
|               | PUT    | `/admin/order/{id}`         | Actualiza el estado de una orden (ADMIN).       |

---

### **Tecnologías Usadas**

- **Framework**: Spring Boot
- **Seguridad**: Spring Security, JWT
- **Base de datos**: MySQL
- **ORM**: JPA (Hibernate)
- **Validaciones**: Bean Validation (Jakarta Validation)
- **Testing**: JUnit, Mockito

---

### **Pruebas**

1. **Postman**: Puedes importar la colección de Postman incluida en este proyecto.
2. **JUnit**: Ejecuta las pruebas con:

   ```bash
   mvn test
   ```

---

### **Contribución**

1. Haz un fork del proyecto.
2. Crea una rama con tus cambios:

   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```

3. Envía un pull request cuando termines.

---

### **Licencia**

Este proyecto está licenciado bajo la [MIT License](LICENSE).

---

🎉 ¡Gracias por contribuir y usar este proyecto!
