package ar.com.colegiotrabsociales.administracion.repository.factura;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FacturaRepository extends JpaRepository<Factura, UUID> {

    Optional<Factura> findByNumero(Long numero);
}
