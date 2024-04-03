package ar.com.colegiotrabsociales.administracion.repository.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MatriculadoRepository extends JpaRepository<Matriculado, UUID> {

    Optional<Matriculado> findByNumero(Long numero);
}
