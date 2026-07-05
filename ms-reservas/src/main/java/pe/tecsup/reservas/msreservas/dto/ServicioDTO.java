package pe.tecsup.reservas.msreservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionMinutos;
    private BigDecimal precio;
}
