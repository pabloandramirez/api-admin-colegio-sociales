package ar.com.colegiotrabsociales.administracion.repository.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatriculadoRepository extends JpaRepository<Matriculado, UUID> {

    Optional<Matriculado> findByNumeroMatricula(Long numero);

    List<Matriculado> findByDniContainingAndNumeroMatriculaContainingAndNombresApellidosContaining(Integer dni, Integer numero, String nombreApellido);
}
