package ar.com.colegiotrabsociales.administracion.mapper.factura;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;

public interface FacturaMapper {

    Factura facturaDTOtoFactura(FacturaDTO facturaDTO);

    FacturaDTO facturaToFacturaDTO(Factura factura);
}
