package ar.com.colegiotrabsociales.administracion.mapper.usuario.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.mapper.usuario.UsuarioMapper;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsuarioMapperImpl implements UsuarioMapper {

    private PasswordEncoder passwordEncoder;
    private final MatriculadoRepository matriculadoRepository;

    @Override
    public Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO) {
        Usuario.UsuarioBuilder builder = Usuario
                .builder()
                .uuid(UUID.randomUUID())
                .usuario(usuarioDTO.getUsuario())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .roles(usuarioDTO.getRoles().stream().map(this::getRole).collect(Collectors.toSet()));

        return builder.build();
    }

    @Override
    public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {

        UsuarioDTO.UsuarioDTOBuilder builder = UsuarioDTO
                .builder()
                .usuario(usuario.getUsuario())
                .roles(usuario.getRoles().stream().map(this::getRole).collect(Collectors.toSet()));

        if (usuario.getMatriculado()!=null){
            builder.numeroMatriculado(String.valueOf(usuario.getMatriculado().getNumeroMatricula()));
        }

        return builder.build();
    }

    private Role getRole(String roleString){
        if(!roleString.isBlank()){
            for (Role role: Role.values()) {
                if (role.getRole().equalsIgnoreCase(roleString)) {
                    return role;
                }
            }
        }
        return null;
    }

    private String getRole(Role role){
        return role.getRole();
    }
}
