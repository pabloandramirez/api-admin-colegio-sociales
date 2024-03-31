package ar.com.colegiotrabsociales.administracion.mapper.cuota;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;

public interface CuotaMapper {

    Cuota cuotaDTOtoCuota(CuotaDTO cuotaDTO);

    CuotaDTO cuotaToCuotaDTO(Cuota cuota);
}
