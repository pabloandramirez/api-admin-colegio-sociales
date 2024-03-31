package ar.com.colegiotrabsociales.administracion.repository.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatriculadoRepository extends JpaRepository<Matriculado, UUID> {
}
