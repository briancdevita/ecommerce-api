package com.example.ecommerce.exception.error;


import org.springframework.http.HttpStatus;

public enum ApiError {


    // ==== Errores de Validación ====

    /**
     * Se usa cuando la solicitud contiene datos inválidos o malformados.
     * Ejemplo: Campos requeridos faltantes o tipos de datos incorrectos.
     */
    INVALID_REQUEST("Invalid request data", HttpStatus.BAD_REQUEST),


    NOT_FOUND("Not found", HttpStatus.NOT_FOUND),
    /**
     * Se usa cuando los datos enviados no cumplen con las reglas de negocio.
     * Ejemplo: El precio de un producto es menor que cero.
     */
    BUSINESS_RULE_VIOLATION("Business rule violation", HttpStatus.BAD_REQUEST),

    // ==== Errores de Recursos (Entidad) ====

    /**
     * Se usa cuando no se encuentra el recurso solicitado en el sistema.
     * Ejemplo: Producto con ID 123 no existe.
     */
    RESOURCE_NOT_FOUND("The requested resource was not found", HttpStatus.NOT_FOUND),

    /**
     * Se usa cuando se intenta crear un recurso que ya existe.
     * Ejemplo: Crear un producto con un nombre ya existente.
     */
    RESOURCE_ALREADY_EXISTS("The resource already exists", HttpStatus.CONFLICT),

    // ==== Errores de Autenticación y Autorización ====

    /**
     * Se usa cuando el cliente no proporciona credenciales válidas.
     * Ejemplo: No se incluye un token de autenticación o el token es inválido.
     */
    UNAUTHORIZED("Unauthorized access", HttpStatus.UNAUTHORIZED),

    /**
     * Se usa cuando el cliente intenta acceder a un recurso sin los permisos necesarios.
     * Ejemplo: Un usuario intenta eliminar un producto, pero solo tiene permisos de lectura.
     */
    FORBIDDEN("Access to this resource is forbidden", HttpStatus.FORBIDDEN),

    // ==== Errores de Sistema ====

    /**
     * Se usa cuando ocurre un error interno inesperado.
     * Ejemplo: Excepción en la base de datos o falla en un servicio externo.
     */
    INTERNAL_SERVER_ERROR("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Se usa cuando el sistema o un servicio externo tarda demasiado en responder.
     * Ejemplo: Límite de tiempo de consulta a la base de datos excedido.
     */
    SERVICE_UNAVAILABLE("The service is currently unavailable", HttpStatus.SERVICE_UNAVAILABLE),

    // ==== Errores relacionados con la solicitud ====


    METHOD_NOT_ALLOWED("The HTTP method is not allowed", HttpStatus.METHOD_NOT_ALLOWED),

    /**
     * Se usa cuando el cliente intenta acceder a un recurso con un formato de respuesta no soportado.
     * Ejemplo: Solicitar `application/xml` cuando solo se admite `application/json`.
     */
    UNSUPPORTED_MEDIA_TYPE("The media type is not supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    /**
     * Se usa cuando un endpoint recibe demasiadas solicitudes en poco tiempo.
     * Ejemplo: Protección contra ataques de fuerza bruta o DoS.
     */
    TOO_MANY_REQUESTS("Too many requests, please try again later", HttpStatus.TOO_MANY_REQUESTS);

    // ==== Atributos del Enum ====

    private final String message;
    private final HttpStatus status;

    ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
