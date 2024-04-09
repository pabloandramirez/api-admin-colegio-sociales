package ar.com.colegiotrabsociales.administracion.mapper.cuota.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CuotaMapperImpl implements CuotaMapper {
    @Override
    public Cuota cuotaDTOtoCuota(CuotaDTO cuotaDTO) {
        return Cuota.builder()
                .uuid(UUID.randomUUID())
                .numero(Long.valueOf(cuotaDTO.getNumeroCuota()))
                .monto(Long.valueOf(cuotaDTO.getMonto()))
                .fechaVencimiento(getLocalDate(cuotaDTO.getFechaVencimientoString()))
                .pagoEstado(getPagoEstado(cuotaDTO.getPagoEstado()))
                .build();
    }

    @Override
    public CuotaDTO cuotaToCuotaDTO(Cuota cuota) {
        return CuotaDTO.builder()
                .idCuota(String.valueOf(cuota.getUuid()))
                .numeroCuota(String.valueOf(cuota.getNumero()))
                .monto(String.valueOf(cuota.getMonto()))
                .fechaVencimientoDate(cuota.getFechaVencimiento())
                .fechaVencimientoString(getLocalDate(cuota.getFechaVencimiento()))
                .build();
    }

    private String getLocalDate(LocalDate localDate){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return localDate.format(formato);
    }

    private LocalDate getLocalDate(String localDate){
        if (!localDate.isBlank()){
            String[] parts = localDate.split("/");
            return LocalDate.of(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        }
        return null;
    }

    private PagoEstado getPagoEstado(String pagoEstadoString){
        if(!pagoEstadoString.isBlank()){
            for (PagoEstado pagoEstado: PagoEstado.values()) {
                if (pagoEstado.getPagoEstado().equalsIgnoreCase(pagoEstadoString)) {
                    return pagoEstado;
                }
            }
        }
        return null;
    }

    private String getPagoEstado(PagoEstado pagoEstado){
        return pagoEstado.getPagoEstado();
    }
}
