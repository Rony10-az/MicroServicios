package pe.tecsup.reservas.msservicios.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.tecsup.reservas.msservicios.entity.Horario;
import pe.tecsup.reservas.msservicios.service.HorarioService;

@Slf4j
@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping("/{id}")
    public ResponseEntity<Horario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.buscarPorId(id));
    }

    @PutMapping("/{id}/reservar")
    public ResponseEntity<Horario> reservar(@PathVariable Long id) {
        log.info("Marcando horario {} como reservado (no disponible)", id);
        return ResponseEntity.ok(horarioService.marcarComoReservado(id));
    }

    @PutMapping("/{id}/liberar")
    public ResponseEntity<Horario> liberar(@PathVariable Long id) {
        log.info("Liberando horario {}", id);
        return ResponseEntity.ok(horarioService.liberar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
