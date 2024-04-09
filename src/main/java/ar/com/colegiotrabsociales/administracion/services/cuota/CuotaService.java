package ar.com.colegiotrabsociales.administracion.services.cuota;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuotaService {
    //POST
    Cuota crearCuota(@RequestBody CuotaDTO cuotaDTO) throws NotFoundException;

    //GET
    List<CuotaDTO> verCuotas();

    //PUT
    Optional<CuotaDTO> actualizarCuota(UUID idCuota, CuotaDTO cuotaActualizada);
    boolean actualizarCuotas(Categoria categoria, BecadoMonotributista becadoMonotributista, Long monto);

    //DELETE
    boolean borrarCuota(UUID idCuota);

}
