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
    private String metodoPago;
    private String numeroFactura;
    private String numeroMatriculado;
    private String fechaPago;
    private LocalDate fechaPagoLocalDate;
    private String linkComprobante;
}
