package pe.tecsup.reservas.msreservas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.tecsup.reservas.msreservas.dto.ClienteDTO;
import pe.tecsup.reservas.msreservas.dto.CrearReservaRequest;
import pe.tecsup.reservas.msreservas.dto.HorarioDTO;
import pe.tecsup.reservas.msreservas.dto.ServicioDTO;
import pe.tecsup.reservas.msreservas.entity.EstadoReserva;
import pe.tecsup.reservas.msreservas.entity.Reserva;
import pe.tecsup.reservas.msreservas.exception.ResourceNotFoundException;
import pe.tecsup.reservas.msreservas.repository.ReservaRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteIntegrationService clienteIntegrationService;
    private final ServicioIntegrationService servicioIntegrationService;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
    }

    public List<Reserva> historialPorCliente(Long clienteId) {
        // Se valida que el cliente exista consultando ms-clientes (con circuit breaker + retry)
        clienteIntegrationService.obtenerCliente(clienteId);
        return reservaRepository.findByClienteIdOrderByFechaCreacionDesc(clienteId);
    }

    @Transactional
    public Reserva crear(CrearReservaRequest request) {
        ClienteDTO cliente = clienteIntegrationService.obtenerCliente(request.getClienteId());
        log.info("Cliente validado para la reserva: {}", cliente.getEmail());

        ServicioDTO servicio = servicioIntegrationService.obtenerServicio(request.getServicioId());
        log.info("Servicio validado para la reserva: {}", servicio.getNombre());

        HorarioDTO horario = servicioIntegrationService.obtenerHorario(request.getHorarioId());
        if (!horario.isDisponible()) {
            throw new IllegalArgumentException("El horario seleccionado ya no esta disponible");
        }

        servicioIntegrationService.reservarHorario(request.getHorarioId());

        Reserva reserva = new Reserva();
        reserva.setClienteId(request.getClienteId());
        reserva.setServicioId(request.getServicioId());
        reserva.setHorarioId(request.getHorarioId());
        reserva.setEstado(EstadoReserva.PENDIENTE);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva confirmar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden confirmar reservas en estado PENDIENTE");
        }
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva cancelar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("La reserva ya se encuentra cancelada");
        }
        servicioIntegrationService.liberarHorario(reserva.getHorarioId());
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva atender(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalArgumentException("Solo se pueden atender reservas en estado CONFIRMADA");
        }
        reserva.setEstado(EstadoReserva.ATENDIDA);
        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        Reserva existente = buscarPorId(id);
        reservaRepository.delete(existente);
    }
}
