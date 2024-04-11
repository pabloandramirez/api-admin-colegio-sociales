package ar.com.colegiotrabsociales.administracion.controllers.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import ar.com.colegiotrabsociales.administracion.services.matriculado.MatriculadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/matriculado")
@RequiredArgsConstructor
@Slf4j
public class MatriculadoController {

    private final MatriculadoService matriculadoService;

    //GET
    @GetMapping("/")
    public List<MatriculadoDTO> getMatriculadoPorNombreNumMatriculaDNI(@RequestParam(value = "nombreApellido", required = false)
                                                                     String nombreApellido,
                                                                 @RequestParam(value = "dni", required = false)
                                                                 String dni,
                                                                 @RequestParam(value = "numeroMatricula", required = false)
                                                                     String numeroMatricula){
        log.info("Muestra el/los matrticulado/s por numero de matricula y/o numero de dni y/o nombre y/o apellido");
        if (nombreApellido==null || nombreApellido.isBlank() && dni==null || dni.isBlank() && numeroMatricula==null || numeroMatricula.isBlank()) {
            return matriculadoService.conseguirMatriculados();
        } else {
            if (matriculadoService.conseguirMatriculadoPorDNIyNumeroyNombreApellido(Long.valueOf(dni), Long.valueOf(numeroMatricula), nombreApellido).isEmpty()){
                log.info("No se consiguio matriculado con estos datos: " + dni + ", " + numeroMatricula +", " + nombreApellido);
            }
        }
        return matriculadoService.conseguirMatriculadoPorDNIyNumeroyNombreApellido(Long.valueOf(dni), Long.valueOf(numeroMatricula), nombreApellido);
    }


    //POST
    @PostMapping("")
    public ResponseEntity<Void> crearMatriculado(@RequestBody MatriculadoDTO matriculadoDTO){
        log.info("Se crea un nuevo matriculado");
        Matriculado matriculadoCreado = matriculadoService.crearMatriculado(matriculadoDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/" + matriculadoCreado.getNumeroMatricula());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/actualizarMatriculado/{uuidMatriculado}")
    public ResponseEntity<Void> actualizarMatriculado(@PathVariable(value = "uuidMatriculado") UUID uuidMatriculado,
                                                      @RequestBody MatriculadoDTO matriculadoActualizado)
            throws NotFoundException {
        Optional<MatriculadoDTO> matriculadoOptional = matriculadoService.actualizarMatriculado(uuidMatriculado, matriculadoActualizado);
        if (matriculadoOptional.isEmpty()){
            log.info("No se encontro el matriculado");
            throw new NotFoundException();
        } else {
            log.info("Se actualizo la información del matriculado");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //DELETE
    @DeleteMapping("/borrarMatriculado/{uuidMatriculado}")
    public ResponseEntity<Void> borrarMatriculado(@PathVariable(value = "uuidMatriculado") UUID uuidMatriculado)
            throws NotFoundException {
        boolean isMatriculadoBorrado = matriculadoService.borrarMatriculado(uuidMatriculado);
        if (isMatriculadoBorrado){
            log.info("Matriculado eliminado");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Matriculado no encontrado");
            throw new NotFoundException();
        }
    }
}
