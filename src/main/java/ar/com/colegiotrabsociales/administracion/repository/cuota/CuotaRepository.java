package ar.com.colegiotrabsociales.administracion.repository.cuota;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, UUID> {

    List<Cuota> findByMatriculadoDniContainingAndMatriculadoNumeroMatriculaContaining(Serializable serializable, Serializable serializable1);
}
