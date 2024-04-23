package ar.com.colegiotrabsociales.administracion.model.factura;

import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import com.sun.jdi.DoubleValue;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturaDTO {
    private String idFactura;
    private String numeroFactura;
    private String monto;
    private Double montoDouble;
    private String anio;
    private Integer anioInt;
    private String enConvenio;
    private String numeroMatriculado;
    private String pagoEstado;
    private List<CuotaDTO> cuotas;
}
