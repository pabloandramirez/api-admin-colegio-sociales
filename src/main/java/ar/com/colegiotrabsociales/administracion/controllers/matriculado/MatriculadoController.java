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
    public List<MatriculadoDTO> getMatriculados(@RequestParam(value = "nombreApellido", required = false)
                                                    String nombreApellido){
        log.info("Muestra los matriculados por nombre/apellido, si no se ingresa por nombre muestra todos");
        if (nombreApellido == null || nombreApellido.isBlank()){
            return matriculadoService.conseguirMatriculados();
        } else {
            if (matriculadoService.conseguirMatriculadoPorNombreApellido(nombreApellido).isEmpty()){
                log.info("No se encontraron matriculados con este nombre/apellido: " + nombreApellido);
            }
        }
        return matriculadoService.conseguirMatriculadoPorNombreApellido(nombreApellido);
    }

    @GetMapping("/{numeroMatricula}")
    public List<MatriculadoDTO> getMatriculadoPorNumeroMatricula(@PathVariable(value = "numeroMatricula") Long numeroMatricula){
        log.info("Muestra el/los matrticulado/s por numero de matricula");
        if (matriculadoService.conseguirMatriculadoPorNumero(numeroMatricula).isEmpty()){
            log.info("No existe matriculados con éste numero");
        }
        return matriculadoService.conseguirMatriculadoPorNumero(numeroMatricula);
    }

    @GetMapping("{dni}")
    public List<MatriculadoDTO> getMatriculadoPorDNI(@PathVariable(value = "dni") Long numeroDNI){
        log.info("Busca matriculados con coincidencia en el DNI");
        if (matriculadoService.conseguirMatriculadoPorDNI(numeroDNI).isEmpty()){
            log.info("No hay matriculado/s que coincidan con este número");
        }
        return matriculadoService.conseguirMatriculadoPorDNI(numeroDNI);
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
    @PutMapping("/{uuidMatriculado}")
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
    @DeleteMapping("/uuidMatriculado")
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
