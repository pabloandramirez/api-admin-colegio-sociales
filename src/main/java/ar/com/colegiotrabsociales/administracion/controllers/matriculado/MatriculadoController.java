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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<MatriculadoDTO> getMatriculadoPorNombreNumMatriculaDNI(@RequestParam(value = "nombres", required = false)
                                                                     String nombres,
                                                                       @RequestParam(value = "apellidos", required = false)
                                                                       String apellidos,
                                                                 @RequestParam(value = "dni", required = false)
                                                                 String dni,
                                                                 @RequestParam(value = "numeroMatricula", required = false)
                                                                     String numeroMatricula){
        log.info("Muestra el/los matrticulado/s por numero de matricula y/o numero de dni y/o nombre y/o apellido");
        if ((nombres == null || nombres.isBlank()) &&
                (apellidos == null || apellidos.isBlank()) &&
                (dni == null || dni.isBlank()) &&
                (numeroMatricula == null || numeroMatricula.isBlank())) {
            return matriculadoService.conseguirMatriculados();
        }

        Integer dniInteger = null;
        Integer numeroMatriculaInteger = null;

        if (dni != null && !dni.isEmpty()) {
            try {
                dniInteger = Integer.parseInt(dni.trim());
            } catch (NumberFormatException e) {
                log.error("Error al convertir dni a Long: {}", e.getMessage());
                // Puedes devolver una lista vacía o manejar el error de otra forma
                return Collections.emptyList();
            }
        }

        if (numeroMatricula != null && !numeroMatricula.isEmpty()) {
            try {
                numeroMatriculaInteger = Integer.parseInt(numeroMatricula.trim());
            } catch (NumberFormatException e) {
                log.error("Error al convertir numeroMatricula a Long: {}", e.getMessage());
                // Puedes devolver una lista vacía o manejar el error de otra forma
                return Collections.emptyList();
            }
        }

        return matriculadoService.conseguirMatriculadoPorDNIyNumeroyNombreApellido(dniInteger, numeroMatriculaInteger, nombres, apellidos);
    }

    @GetMapping("/paginado")
    public List<MatriculadoDTO> getMatriculadosPaginado(@RequestParam(name = "pagina", required = true) int pagina,
                                                @RequestParam(name = "matriculadosPorPagina", required = true) int matriculadosPorPagina){
        log.info("Se muestra los matriculados en formato de paginado");
        // Calcular el índice de inicio de los matriculados en función de la página y la cantidad de matriculados por página
        int indiceInicio = (pagina - 1) * matriculadosPorPagina;

        // Obtener las noticias para la página actual

        return matriculadoService.getMatriculadosPaginados(indiceInicio, matriculadosPorPagina);

    }

    @GetMapping("/{usuario}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public List<MatriculadoDTO> getMatriculadoPorUsuario(@PathVariable(value = "usuario") String usuario){
        log.info("Muestra el matriculado por nombre de usuario");
        if (matriculadoService.conseguirMatriculadoPorUsuario(usuario).isEmpty()){
            log.info("No hay matriculados con ese usuario");
        }

        return matriculadoService.conseguirMatriculadoPorUsuario(usuario);
    }


    //POST
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> crearMatriculado(@RequestBody MatriculadoDTO matriculadoDTO){
        log.info("Se crea un nuevo matriculado");
        Matriculado matriculadoCreado = matriculadoService.crearMatriculado(matriculadoDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/" + matriculadoCreado.getNumeroMatricula());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/actualizarMatriculado/{uuidMatriculado}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
