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
    private String numeroFactura;
    private String enConvenio;
    private String numeroMatriculado;
}
