package ar.com.colegiotrabsociales.administracion.controllers.factura;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.services.factura.FacturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/factura")
@RequiredArgsConstructor
@Slf4j
public class FacturaController {

    private final FacturaService facturaService;

    //GET
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FacturaDTO> getFacturas(@RequestParam(value = "dni", required = false) String dniMatriculado,
                                        @RequestParam(value = "numeroMatricula", required = false) String numeroMatricula){
        log.info("Busca las facturas por dni y/o numero del matriculado");
        if (dniMatriculado==null || dniMatriculado.isBlank() && numeroMatricula==null || numeroMatricula.isBlank()){
            return facturaService.verFacturas();
        } else {
            if (facturaService.verFacturasPorDNIoNumeroMatricula(Long.valueOf(dniMatriculado), Long.valueOf(numeroMatricula)).isEmpty()){
                log.warn("No hay facturas con este dni y/o numero de matriculado");
            }
        }
        return facturaService.verFacturasPorDNIoNumeroMatricula(Long.valueOf(dniMatriculado), Long.valueOf(numeroMatricula));
    }

    //POST
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> crearFactura(@RequestBody FacturaDTO facturaDTO) throws NotFoundException {
        log.info("Se crea una nueva factura");
        Factura facturaCreada = facturaService.crearFactura(facturaDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/" + facturaCreada.getUuid());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping("/crearFacturas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> crearFacturas(@RequestBody FacturaDTO facturaDTO,
                                             @RequestParam(name = "categoria", required = true) String categoria,
                                              @RequestParam(name = "becadoMono", required = false) String becadoMono)
            throws NotFoundException {
        log.info("Se crea una nueva factura");

        String becadoMonoCorreccion;

        if (becadoMono==null || becadoMono.isBlank()){
            becadoMonoCorreccion = "NO CORRESPONDE";
        } else {
            becadoMonoCorreccion = becadoMono;
        }

        List<Factura> facturasCreadas = facturaService.crearFacturas(facturaDTO, categoria, becadoMonoCorreccion);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Created", "True");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/actualizarFactura/{uuidFactura}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarFactura(@PathVariable(value = "uuidFactura") UUID uuidFactura,
                                                      @RequestBody FacturaDTO facturaActualizada)
            throws NotFoundException {
        Optional<FacturaDTO> facturaDTOOptional = facturaService.actualizarFactura(uuidFactura, facturaActualizada);
        if (facturaDTOOptional.isEmpty()){
            log.info("No se encontro la factura");
            throw new NotFoundException();
        } else {
            log.info("Se actualizo la información dela factura");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //DELETE
    @DeleteMapping("/borrarFactura/{uuidFactura}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> borrarFactura(@PathVariable(value = "uuidFactura") UUID uuidFactura)
            throws NotFoundException {
        boolean isFacturaBorrada = facturaService.borrarFactura(uuidFactura);
        if (isFacturaBorrada){
            log.info("Factura eliminada");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Factura no encontrada");
            throw new NotFoundException();
        }
    }
}
