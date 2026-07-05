package pe.tecsup.reservas.msreservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pe.tecsup.reservas.msreservas.dto.HorarioDTO;
import pe.tecsup.reservas.msreservas.dto.ServicioDTO;

@FeignClient(name = "ms-servicios")
public interface ServicioClient {

    @GetMapping("/api/servicios/{id}")
    ServicioDTO obtenerServicio(@PathVariable("id") Long id);

    @GetMapping("/api/horarios/{id}")
    HorarioDTO obtenerHorario(@PathVariable("id") Long id);

    @PutMapping("/api/horarios/{id}/reservar")
    HorarioDTO reservarHorario(@PathVariable("id") Long id);

    @PutMapping("/api/horarios/{id}/liberar")
    HorarioDTO liberarHorario(@PathVariable("id") Long id);
}
