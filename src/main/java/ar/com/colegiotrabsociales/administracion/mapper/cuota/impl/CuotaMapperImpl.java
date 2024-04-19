package ar.com.colegiotrabsociales.administracion.mapper.cuota.impl;

import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CuotaMapperImpl implements CuotaMapper {
    @Override
    public Cuota cuotaDTOtoCuota(CuotaDTO cuotaDTO) {
        return Cuota.builder()
                .uuid(UUID.randomUUID())
                .numero(Integer.parseInt(cuotaDTO.getNumeroCuota().trim()))
                .monto(Double.valueOf(cuotaDTO.getMonto().trim()))
                .fechaPago(getLocalDate(cuotaDTO.getFechaPagoString()))
                .linkComprobante(cuotaDTO.getLinkComprobante().trim())
                .build();
    }

    @Override
    public CuotaDTO cuotaToCuotaDTO(Cuota cuota) {
        CuotaDTO.CuotaDTOBuilder builder = CuotaDTO.builder()
                .idCuota(String.valueOf(cuota.getUuid()))
                .numeroCuota(String.valueOf(cuota.getNumero()))
                .monto(String.valueOf(cuota.getMonto()))
                .numeroFactura(String.valueOf(cuota.getFactura().getNumero()))
                .numeroMatriculado(String.valueOf(cuota.getMatriculado().getNumeroMatricula()))
                .fechaPagoString(getLocalDate(cuota.getFechaPago()))
                .fechaPagoLocalDate(cuota.getFechaPago());


        if (cuota.getLinkComprobante()!=null){
            builder.linkComprobante(cuota.getLinkComprobante());
        }

        return builder.build();
    }

    private String getLocalDate(LocalDate localDate){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return localDate.format(formato);
    }

    private LocalDate getLocalDate(String localDate){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(localDate, formato);
    }

}
