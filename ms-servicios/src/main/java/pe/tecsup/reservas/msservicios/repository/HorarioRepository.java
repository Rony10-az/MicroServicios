package pe.tecsup.reservas.msservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tecsup.reservas.msservicios.entity.Horario;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByServicioId(Long servicioId);
}
