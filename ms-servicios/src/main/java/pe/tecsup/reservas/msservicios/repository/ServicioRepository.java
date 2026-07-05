package pe.tecsup.reservas.msservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tecsup.reservas.msservicios.entity.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
