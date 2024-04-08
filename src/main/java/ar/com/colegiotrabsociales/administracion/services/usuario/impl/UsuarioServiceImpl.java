package ar.com.colegiotrabsociales.administracion.services.usuario.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.mapper.usuario.UsuarioMapper;
import ar.com.colegiotrabsociales.administracion.model.usuario.UsuarioDTO;
import ar.com.colegiotrabsociales.administracion.repository.usuario.UsuarioRepository;
import ar.com.colegiotrabsociales.administracion.services.usuario.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioMapper usuarioMapper;

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioDTO> getUsuarios() {
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        for (Usuario usuario: usuarioRepository.findAll()){
            usuarioDTOList.add(usuarioMapper.usuarioToUsuarioDTO(usuario));
        }
        return usuarioDTOList;
    }

    @Override
    public List<UsuarioDTO> getUsuarioPorNombre(String nombre) {
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        for (Usuario usuario: usuarioRepository.findAll()){
            if (usuario.getUsuario().toLowerCase().contains(nombre.toLowerCase())){
                usuarioDTOList.add(usuarioMapper.usuarioToUsuarioDTO(usuario));
            }
        }
        return usuarioDTOList;
    }

    @Override
    public Usuario crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuarioCreado = usuarioMapper.usuarioDTOtoUsuario(usuarioDTO);
        return usuarioRepository.save(usuarioCreado);
    }

    @Override
    public boolean borrarNoticia(UUID idUsuario) {
        if(usuarioRepository.existsById(idUsuario)){
            usuarioRepository.deleteById(idUsuario);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UsuarioDTO> actualizarUsuario(UUID idUsuario, UsuarioDTO usuarioActualizadoDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isPresent()){
            actualizacionUsuario(usuarioOptional.get(), usuarioActualizadoDTO);
            usuarioRepository.saveAndFlush(usuarioOptional.get());
            return Optional.of(usuarioMapper.usuarioToUsuarioDTO(usuarioOptional.get()));
        } else {
            return Optional.empty();
        }
    }

    private void actualizacionUsuario(Usuario usuario, UsuarioDTO usuarioActualizadoDTO){
        if (usuarioActualizadoDTO.getUsuario() != null && !usuarioActualizadoDTO.getUsuario().isBlank()){
            usuario.setUsuario(usuarioActualizadoDTO.getUsuario());
        }

        if (usuarioActualizadoDTO.getRoles() != null && !usuarioActualizadoDTO.getRoles().isEmpty()){
            usuario.setRoles(usuarioActualizadoDTO.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()));
        }
    }
}
