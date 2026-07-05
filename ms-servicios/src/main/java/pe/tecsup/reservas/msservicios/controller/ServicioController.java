package pe.tecsup.reservas.msservicios.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.tecsup.reservas.msservicios.dto.CrearHorarioRequest;
import pe.tecsup.reservas.msservicios.entity.Horario;
import pe.tecsup.reservas.msservicios.entity.Servicio;
import pe.tecsup.reservas.msservicios.service.HorarioService;
import pe.tecsup.reservas.msservicios.service.ServicioService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;
    private final HorarioService horarioService;

    @Value("${app.display-name:ms-servicios}")
    private String displayName;

    @Value("${app.mensajes.bienvenida:Microservicio de servicios activo}")
    private String mensajeBienvenida;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        log.info("Ping recibido en {}", displayName);
        return ResponseEntity.ok(Map.of("servicio", displayName, "mensaje", mensajeBienvenida));
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Servicio> crear(@Valid @RequestBody Servicio servicio) {
        log.info("Creando nuevo servicio: {}", servicio.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @Valid @RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.actualizar(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{servicioId}/horarios")
    public ResponseEntity<List<Horario>> listarHorarios(@PathVariable Long servicioId) {
        return ResponseEntity.ok(horarioService.listarPorServicio(servicioId));
    }

    @PostMapping("/{servicioId}/horarios")
    public ResponseEntity<Horario> crearHorario(@PathVariable Long servicioId, @Valid @RequestBody CrearHorarioRequest request) {
        log.info("Creando horario para servicio {}", servicioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.crear(servicioId, request));
    }
}
