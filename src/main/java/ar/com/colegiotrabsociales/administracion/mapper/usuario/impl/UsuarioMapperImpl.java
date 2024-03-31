package ar.com.colegiotrabsociales.administracion.mapper.usuario.impl;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.mapper.usuario.UsuarioMapper;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperImpl implements UsuarioMapper {
    @Override
    public Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {
        return null;
    }
}
