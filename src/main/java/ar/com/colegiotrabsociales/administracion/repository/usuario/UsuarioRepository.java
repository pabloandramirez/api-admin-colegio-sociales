package ar.com.colegiotrabsociales.administracion.repository.usuario;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByUsuario(String usuario);
}
