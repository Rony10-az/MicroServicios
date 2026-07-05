package pe.tecsup.reservas.msclientes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.tecsup.reservas.msclientes.entity.Cliente;
import pe.tecsup.reservas.msclientes.service.ClienteService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Value("${app.display-name:ms-clientes}")
    private String displayName;

    @Value("${app.mensajes.bienvenida:Microservicio de clientes activo}")
    private String mensajeBienvenida;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        log.info("Ping recibido en {}", displayName);
        return ResponseEntity.ok(Map.of("servicio", displayName, "mensaje", mensajeBienvenida));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        log.info("Listando todos los clientes");
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Long id) {
        log.info("Consultando cliente con id {}", id);
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> registrar(@Valid @RequestBody Cliente cliente) {
        log.info("Registrando nuevo cliente: {}", cliente.getEmail());
        Cliente creado = clienteService.registrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        log.info("Actualizando cliente con id {}", id);
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando cliente con id {}", id);
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
