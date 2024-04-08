package ar.com.colegiotrabsociales.administracion.services.usuario;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {

    List<UsuarioDTO> getUsuarios();

    List<UsuarioDTO> getUsuarioPorNombre(String nombre);

    Usuario crearUsuario(@RequestBody UsuarioDTO usuarioDTO);

    boolean borrarNoticia(UUID idUsuario);

    Optional<UsuarioDTO> actualizarUsuario(UUID idUsuario, UsuarioDTO usuarioActualizadoDTO);
}
