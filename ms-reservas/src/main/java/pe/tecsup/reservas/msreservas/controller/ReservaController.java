package pe.tecsup.reservas.msreservas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.tecsup.reservas.msreservas.dto.CrearReservaRequest;
import pe.tecsup.reservas.msreservas.entity.Reserva;
import pe.tecsup.reservas.msreservas.service.ReservaService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @Value("${app.display-name:ms-reservas}")
    private String displayName;

    @Value("${app.mensajes.bienvenida:Microservicio de reservas activo}")
    private String mensajeBienvenida;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        log.info("Ping recibido en {}", displayName);
        return ResponseEntity.ok(Map.of("servicio", displayName, "mensaje", mensajeBienvenida));
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}/historial")
    public ResponseEntity<List<Reserva>> historial(@PathVariable Long clienteId) {
        log.info("Consultando historial de reservas del cliente {}", clienteId);
        return ResponseEntity.ok(reservaService.historialPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@Valid @RequestBody CrearReservaRequest request) {
        log.info("Creando reserva para cliente {} - servicio {} - horario {}",
                request.getClienteId(), request.getServicioId(), request.getHorarioId());
        Reserva creada = reservaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmar(@PathVariable Long id) {
        log.info("Confirmando reserva {}", id);
        return ResponseEntity.ok(reservaService.confirmar(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        log.info("Cancelando reserva {}", id);
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<Reserva> atender(@PathVariable Long id) {
        log.info("Marcando reserva {} como atendida", id);
        return ResponseEntity.ok(reservaService.atender(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
