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
    public List<FacturaDTO> getFacturas(@RequestParam(name = "dni", required = false) Integer dniMatriculado,
                                        @RequestParam(name = "numeroMatricula", required = false) Integer numeroMatricula,
                                        @RequestParam(name = "pagina", required = false) Integer pagina,
                                        @RequestParam(name = "facturasPorPagina", required = false) Integer facturasPorPagina){
        log.info("Busca las facturas por dni y/o numero del matriculado");

        Integer indiceInicio = null;
        if(pagina!=null && !pagina.toString().isBlank()){
            indiceInicio = (pagina - 1) * facturasPorPagina;
        } else {
            indiceInicio = 0;
        }

        if ((dniMatriculado==null || dniMatriculado.toString().isBlank()) && (numeroMatricula==null || numeroMatricula.toString().isBlank())){
            return facturaService.verFacturas();
        } else {
            if (facturaService.verFacturasPorDNIoNumeroMatricula(dniMatriculado, numeroMatricula, indiceInicio, facturasPorPagina).isEmpty()){
                log.warn("No hay facturas con este dni y/o numero de matriculado");
            }
        }


        return facturaService.verFacturasPorDNIoNumeroMatricula(dniMatriculado, numeroMatricula, indiceInicio, facturasPorPagina);
    }

    @GetMapping("/getByCategoria")
    public List<FacturaDTO> getFacturasPorCategoria(@RequestParam(name = "categoria", required = true) String categoria,
                                                    @RequestParam(name = "becadoMono", required = true) String becadoMono,
                                                    @RequestParam(name = "anio", required = true) Integer anio) throws NotFoundException {
        List<FacturaDTO> facturaDTOList = facturaService.verFacturasPorCategoria(categoria, becadoMono, anio);
        if (facturaDTOList.isEmpty()){
            log.warn("No se encontro ninguna factura en esta categoria");
            throw new NotFoundException();
        } else {
            return facturaDTOList;
        }
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
                                              @RequestParam(name = "becadoMono", required = true) String becadoMono)
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

    @PutMapping("/actualizarFacturas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarFacturas(@RequestBody FacturaDTO facturaDTO,
                                                   @RequestParam(name = "categoria", required = true) String categoria,
                                                   @RequestParam(name = "becadoMono", required = true) String becadoMono,
                                                   @RequestParam(name = "anio", required = true) Integer anio)
            throws NotFoundException {
        List<FacturaDTO> facturaDTOList = facturaService.actualizarFacturas(facturaDTO, categoria, becadoMono, anio);
        if (facturaDTOList.isEmpty()){
            log.info("No se encontraron las facturas");
            throw new NotFoundException();
        } else {
            log.info("Se actualizo la información de las facturas");
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
