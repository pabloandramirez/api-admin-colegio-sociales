package ar.com.colegiotrabsociales.administracion.services.factura;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacturaService {

    //POST
    Factura crearFactura(@RequestBody FacturaDTO facturaDTO) throws NotFoundException;

    //GET
    List<FacturaDTO> verFacturas();
    List<FacturaDTO> verFacturasPorDNIoNumeroMatricula(Long dni, Long numeroMatricula);

    //PUT
    Optional<FacturaDTO> actualizarFactura(UUID idFactura, FacturaDTO facturaActualizada);

    //DELETE
    boolean borrarFactura(UUID idFactura);
}
