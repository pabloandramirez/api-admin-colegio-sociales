package ar.com.colegiotrabsociales.administracion.mapper.usuario;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;

public interface UsuarioMapper {

    Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);
}
