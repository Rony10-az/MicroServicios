package pe.tecsup.reservas.msclientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tecsup.reservas.msclientes.entity.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
