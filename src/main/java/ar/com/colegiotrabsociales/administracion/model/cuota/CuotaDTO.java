package ar.com.colegiotrabsociales.administracion.model.cuota;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuotaDTO {
    private String idCuota;
    private Long numeroCuota;
    private Long monto;
    private LocalDate fechaVencimientoDate;
    private String fechaVencimientoString;
    private String pagoEstado;
    private String factura;
}
