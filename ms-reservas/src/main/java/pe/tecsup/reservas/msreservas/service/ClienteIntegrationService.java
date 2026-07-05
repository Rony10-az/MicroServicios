package pe.tecsup.reservas.msreservas.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pe.tecsup.reservas.msreservas.client.ClienteClient;
import pe.tecsup.reservas.msreservas.dto.ClienteDTO;
import pe.tecsup.reservas.msreservas.exception.ServicioNoDisponibleException;

/**
 * Encapsula la comunicacion con ms-clientes aplicando Circuit Breaker y Retry (Resilience4j).
 * El nombre de instancia "ms-clientes" coincide con el spring.application.name del servicio destino.
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
public class ClienteIntegrationService {

    private static final String MENSAJE_FALLBACK_DEFAULT = "No fue posible consultar el servicio. Intente nuevamente.";
    private static volatile String mensajeFallbackCache = MENSAJE_FALLBACK_DEFAULT;

    private final ClienteClient clienteClient;
    private final Environment environment;

    @PostConstruct
    void init() {
        mensajeFallbackCache = environment.getProperty("app.mensajes.fallback", MENSAJE_FALLBACK_DEFAULT);
    }

    @CircuitBreaker(name = "ms-clientes", fallbackMethod = "clienteFallback")
    @Retry(name = "ms-clientes")
    public ClienteDTO obtenerCliente(Long clienteId) {
        return clienteClient.obtenerCliente(clienteId);
    }

    private ClienteDTO clienteFallback(Long clienteId, Throwable throwable) {
        log.error("Fallback activado al consultar ms-clientes (id={}): {}", clienteId, throwable.toString());
        throw new ServicioNoDisponibleException(mensajeFallbackCache);
    }
}
