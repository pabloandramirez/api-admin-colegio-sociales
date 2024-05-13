package ar.com.colegiotrabsociales.administracion.repository.factura;

import ar.com.colegiotrabsociales.administracion.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, UUID> {


    List<Factura> findByMatriculadoDniContainingAndMatriculadoNumeroMatriculaContaining(Serializable serializable, Serializable serializable1);
}
