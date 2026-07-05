package pe.tecsup.reservas.msclientes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.tecsup.reservas.msclientes.entity.Cliente;
import pe.tecsup.reservas.msclientes.exception.ResourceNotFoundException;
import pe.tecsup.reservas.msclientes.repository.ClienteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    public Cliente registrar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con ese email");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDireccion(datos.getDireccion());
        return clienteRepository.save(existente);
    }

    public void eliminar(Long id) {
        Cliente existente = buscarPorId(id);
        clienteRepository.delete(existente);
    }
}
