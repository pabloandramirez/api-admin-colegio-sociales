package ar.com.colegiotrabsociales.administracion.mapper.usuario.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.mapper.usuario.UsuarioMapper;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsuarioMapperImpl implements UsuarioMapper {

    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO) {
        return Usuario
                .builder()
                .uuid(UUID.randomUUID())
                .usuario(usuarioDTO.getUsuario())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .roles(usuarioDTO.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {
        return UsuarioDTO
                .builder()
                .usuario(usuario.getUsuario())
                .roles(usuario.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
    }
}
