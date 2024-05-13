package ar.com.colegiotrabsociales.administracion.controllers.usuario;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import ar.com.colegiotrabsociales.administracion.services.usuario.UsuarioService;
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
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    //GET
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> getUsuarios(@RequestParam(name = "name", required = false) String nombre){
        log.info("Busca por nombre, si no encuentra muestra todos");
        if (nombre == null || nombre.isBlank()){
            return usuarioService.getUsuarios();
        } else {
            if (usuarioService.getUsuarioPorNombre(nombre).isEmpty()){
                log.info("No hay usuarios con este nombre: " + nombre);
            }
        }
        return usuarioService.getUsuarioPorNombre(nombre);
    }

    //POST
    @PostMapping("/crearUsuario")
    /*@PreAuthorize("hasRole('ADMIN')")*/
    public ResponseEntity<Void> crearUsuario(@RequestBody UsuarioDTO usuarioDTO){
        log.info("Creando un nuevo usuario");
        Usuario usuarioCreado = usuarioService.crearUsuario(usuarioDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/" + usuarioCreado.getUuid());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //DELETE
    @DeleteMapping("/eliminarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable(name = "idUsuario") UUID idUsuario)
            throws NotFoundException {
        boolean isUsuarioBorrado = usuarioService.borrarNoticia(idUsuario);

        if (isUsuarioBorrado){
            log.info("Usuario eliminado");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Usuario encontrado");
            throw new NotFoundException();
        }
    }

    //PUT
    @PutMapping("/actualizarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarUsuario(@PathVariable(name = "idUsuario") UUID idUsuario,
                                                  @RequestBody UsuarioDTO usuarioActualizadoDTO)
            throws NotFoundException {
        Optional<UsuarioDTO> usuarioDTO = usuarioService.actualizarUsuario(idUsuario, usuarioActualizadoDTO);
        if (usuarioDTO.isEmpty()){
            log.info("Usuario no encontrado");
            throw new NotFoundException();
        } else {
            log.info("Usuario actualizado");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
