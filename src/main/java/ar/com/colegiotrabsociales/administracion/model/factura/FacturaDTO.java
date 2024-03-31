package ar.com.colegiotrabsociales.administracion.model.factura;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturaDTO {
    private String idFactura;
    private Long numeroFactura;
    private Long monto;
    private LocalDate fechaVencimientoDate;
    private String fechaVencimientoString;
    private String enConvenio;
    private String matriculado;
    private List<String> cuotaList;
}
