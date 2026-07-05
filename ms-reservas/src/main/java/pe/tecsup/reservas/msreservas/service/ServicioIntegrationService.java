package pe.tecsup.reservas.msreservas.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pe.tecsup.reservas.msreservas.client.ServicioClient;
import pe.tecsup.reservas.msreservas.dto.HorarioDTO;
import pe.tecsup.reservas.msreservas.dto.ServicioDTO;
import pe.tecsup.reservas.msreservas.exception.ServicioNoDisponibleException;

/**
 * Encapsula la comunicacion con ms-servicios aplicando Circuit Breaker y Retry (Resilience4j).
 * El nombre de instancia "ms-servicios" coincide con el spring.application.name del servicio destino.
 *
 * <p>El mensaje de fallback se cachea en un campo ESTATICO a proposito: Resilience4j invoca los
 * metodos de fallback de forma reflectiva sobre una instancia que no siempre pasa por la
 * inyeccion de dependencias normal de Spring, por lo que un campo de instancia (incluso final,
 * incluso via constructor) puede llegar nulo en ese momento. Un campo estatico, en cambio, es
 * compartido por cualquier instancia de la clase.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioIntegrationService {

    private static final String MENSAJE_FALLBACK_DEFAULT = "No fue posible consultar el servicio. Intente nuevamente.";
    private static volatile String mensajeFallbackCache = MENSAJE_FALLBACK_DEFAULT;

    private final ServicioClient servicioClient;
    private final Environment environment;

    @PostConstruct
    void init() {
        mensajeFallbackCache = environment.getProperty("app.mensajes.fallback", MENSAJE_FALLBACK_DEFAULT);
    }

    @CircuitBreaker(name = "ms-servicios", fallbackMethod = "servicioFallback")
    @Retry(name = "ms-servicios")
    public ServicioDTO obtenerServicio(Long servicioId) {
        return servicioClient.obtenerServicio(servicioId);
    }

    @CircuitBreaker(name = "ms-servicios", fallbackMethod = "horarioFallback")
    @Retry(name = "ms-servicios")
    public HorarioDTO obtenerHorario(Long horarioId) {
        return servicioClient.obtenerHorario(horarioId);
    }

    @CircuitBreaker(name = "ms-servicios", fallbackMethod = "horarioFallback")
    @Retry(name = "ms-servicios")
    public HorarioDTO reservarHorario(Long horarioId) {
        return servicioClient.reservarHorario(horarioId);
    }

    @CircuitBreaker(name = "ms-servicios", fallbackMethod = "horarioFallback")
    @Retry(name = "ms-servicios")
    public HorarioDTO liberarHorario(Long horarioId) {
        return servicioClient.liberarHorario(horarioId);
    }

    private ServicioDTO servicioFallback(Long servicioId, Throwable throwable) {
        log.error("Fallback activado al consultar servicio en ms-servicios (id={}): {}", servicioId, throwable.toString());
        throw new ServicioNoDisponibleException(mensajeFallbackCache);
    }

    private HorarioDTO horarioFallback(Long horarioId, Throwable throwable) {
        log.error("Fallback activado al consultar horario en ms-servicios (id={}): {}", horarioId, throwable.toString());
        throw new ServicioNoDisponibleException(mensajeFallbackCache);
    }
}
