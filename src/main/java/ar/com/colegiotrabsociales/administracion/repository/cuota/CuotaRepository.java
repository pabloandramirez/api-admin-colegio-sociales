package ar.com.colegiotrabsociales.administracion.repository.cuota;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CuotaRepository extends JpaRepository<Cuota, UUID> {

    Optional<Cuota> findByNumero(Long numero);
}
