package pe.tecsup.reservas.msreservas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearReservaRequest {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El servicioId es obligatorio")
    private Long servicioId;

    @NotNull(message = "El horarioId es obligatorio")
    private Long horarioId;
}
