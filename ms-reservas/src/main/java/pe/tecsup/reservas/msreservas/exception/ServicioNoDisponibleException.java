package pe.tecsup.reservas.msreservas.exception;

/**
 * Se lanza cuando el circuit breaker / retry agota los intentos
 * contra un microservicio dependiente (ms-clientes o ms-servicios).
 */
public class ServicioNoDisponibleException extends RuntimeException {
    public ServicioNoDisponibleException(String message) {
        super(message);
    }
}
