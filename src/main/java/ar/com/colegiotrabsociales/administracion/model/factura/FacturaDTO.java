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
    private String enConvenio;
    private String idMatriculado;
    private List<String> cuotaList;
}
