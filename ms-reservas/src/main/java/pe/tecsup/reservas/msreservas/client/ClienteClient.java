package pe.tecsup.reservas.msreservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.tecsup.reservas.msreservas.dto.ClienteDTO;

@FeignClient(name = "ms-clientes")
public interface ClienteClient {

    @GetMapping("/api/clientes/{id}")
    ClienteDTO obtenerCliente(@PathVariable("id") Long id);
}
