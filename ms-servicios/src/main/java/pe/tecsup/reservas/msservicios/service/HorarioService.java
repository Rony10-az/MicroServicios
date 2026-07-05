package pe.tecsup.reservas.msservicios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.tecsup.reservas.msservicios.dto.CrearHorarioRequest;
import pe.tecsup.reservas.msservicios.entity.Horario;
import pe.tecsup.reservas.msservicios.entity.Servicio;
import pe.tecsup.reservas.msservicios.exception.ResourceNotFoundException;
import pe.tecsup.reservas.msservicios.repository.HorarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final ServicioService servicioService;

    public List<Horario> listarPorServicio(Long servicioId) {
        return horarioRepository.findByServicioId(servicioId);
    }

    public Horario buscarPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));
    }

    public Horario crear(Long servicioId, CrearHorarioRequest request) {
        Servicio servicio = servicioService.buscarPorId(servicioId);
        Horario horario = new Horario();
        horario.setServicio(servicio);
        horario.setFecha(request.getFecha());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setDisponible(true);
        return horarioRepository.save(horario);
    }

    public Horario marcarComoReservado(Long id) {
        Horario horario = buscarPorId(id);
        if (!horario.isDisponible()) {
            throw new IllegalArgumentException("El horario ya no esta disponible");
        }
        horario.setDisponible(false);
        return horarioRepository.save(horario);
    }

    public Horario liberar(Long id) {
        Horario horario = buscarPorId(id);
        horario.setDisponible(true);
        return horarioRepository.save(horario);
    }

    public void eliminar(Long id) {
        Horario existente = buscarPorId(id);
        horarioRepository.delete(existente);
    }
}
