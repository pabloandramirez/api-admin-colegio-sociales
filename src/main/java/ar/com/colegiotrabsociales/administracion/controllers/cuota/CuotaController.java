package ar.com.colegiotrabsociales.administracion.controllers.cuota;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.services.cuota.CuotaService;
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
@RequestMapping("/cuota")
@RequiredArgsConstructor
@Slf4j
public class CuotaController {

    private final CuotaService cuotaService;

    //GET
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<CuotaDTO> getCuotas(@RequestParam(value = "dni", required = false) String dniMatriculado,
                                    @RequestParam(value = "numeroMatricula", required = false) String numeroMatricula){
        log.info("Busca las cuotas por dni y/o numero del matriculado");
        if (dniMatriculado==null || dniMatriculado.isBlank() && numeroMatricula==null || numeroMatricula.isBlank()){
            return cuotaService.verCuotas();
        } else {
            if (cuotaService.verCuotasPorDniNumeroMatricula(Long.valueOf(dniMatriculado), Long.valueOf(numeroMatricula)).isEmpty()){
                log.warn("No hay cuotas con este dni y/o numero de matriculado");
            }
        }
        return cuotaService.verCuotasPorDniNumeroMatricula(Long.valueOf(dniMatriculado), Long.valueOf(numeroMatricula));
    }

    //POST
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<Void> crearCuota(@RequestBody CuotaDTO cuotaDTO) throws NotFoundException {
        log.info("Se crea una nueva cuota");
        Cuota cuotaCreada = cuotaService.crearCuota(cuotaDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/" + cuotaCreada.getUuid());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizarCuota/{uuidCuota}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<Void> actualizarCuota(@PathVariable(value = "uuidCuota") UUID uuidCuota,
                                                  @RequestBody CuotaDTO cuotaActualizada)
            throws NotFoundException {
        Optional<CuotaDTO> cuotaDTOOptional = cuotaService.actualizarCuota(uuidCuota, cuotaActualizada);
        if (cuotaDTOOptional.isEmpty()){
            log.info("No se encontro la cuota");
            throw new NotFoundException();
        } else {
            log.info("Se actualizo la información dela cuota");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //DELETE
    @DeleteMapping("/borrarCuota/{uuidCuota}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<Void> borrarCuota(@PathVariable(value = "uuidCuota") UUID uuidCuota)
            throws NotFoundException {
        boolean isCuotaBorrada = cuotaService.borrarCuota(uuidCuota);
        if (isCuotaBorrada){
            log.info("Cuota eliminada");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Cuota no encontrada");
            throw new NotFoundException();
        }
    }

}
