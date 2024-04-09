package ar.com.colegiotrabsociales.administracion.model.cuota;

import lombok.*;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuotaDTO {
    private String idCuota;
    private String numeroCuota;
    private String monto;
    private LocalDate fechaVencimientoDate;
    private String fechaVencimientoString;
    private String pagoEstado;
    private String numeroFactura;
    private String numeroMatriculado;
}
