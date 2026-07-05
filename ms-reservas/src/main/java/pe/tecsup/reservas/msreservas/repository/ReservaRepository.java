package pe.tecsup.reservas.msreservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tecsup.reservas.msreservas.entity.Reserva;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteIdOrderByFechaCreacionDesc(Long clienteId);
}
