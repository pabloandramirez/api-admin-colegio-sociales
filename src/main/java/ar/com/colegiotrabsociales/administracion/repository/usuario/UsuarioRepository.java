package ar.com.colegiotrabsociales.administracion.repository.usuario;

import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByUsuario(String usuario);
}
