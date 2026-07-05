package pe.tecsup.reservas.msservicios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.tecsup.reservas.msservicios.entity.Servicio;
import pe.tecsup.reservas.msservicios.exception.ResourceNotFoundException;
import pe.tecsup.reservas.msservicios.repository.ServicioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public Servicio buscarPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
    }

    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio actualizar(Long id, Servicio datos) {
        Servicio existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setDuracionMinutos(datos.getDuracionMinutos());
        existente.setPrecio(datos.getPrecio());
        return servicioRepository.save(existente);
    }

    public void eliminar(Long id) {
        Servicio existente = buscarPorId(id);
        servicioRepository.delete(existente);
    }
}
