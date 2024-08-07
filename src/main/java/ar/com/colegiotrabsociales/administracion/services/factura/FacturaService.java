package ar.com.colegiotrabsociales.administracion.services.factura;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
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

    List<Factura> crearFacturas(@RequestBody FacturaDTO facturaDTO, String categoria, String becadoMono) throws NotFoundException;

    //GET
    List<FacturaDTO> verFacturas();

    Optional<FacturaDTO> facturaByUUID(UUID idFactura);
    List<FacturaDTO> verFacturasPorDNIoNumeroMatricula(Integer dni, Integer numeroMatricula, Integer pagina, Integer facturasPorPagina);
    List<FacturaDTO> verFacturasPorCategoria(String categoria, String becadoMonotributista, Integer anio);

    //PUT
    Optional<FacturaDTO> actualizarFactura(UUID idFactura, FacturaDTO facturaActualizada);

    List<FacturaDTO> actualizarFacturas(FacturaDTO facturaDTO, String categoria, String becadoMono, Integer anio);

    //DELETE
    boolean borrarFactura(UUID idFactura);
}
