package ar.com.colegiotrabsociales.administracion.mapper.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FacturaMapperImpl implements FacturaMapper {

    private final CuotaMapper cuotaMapper;
    @Override
    public Factura facturaDTOtoFactura(FacturaDTO facturaDTO) {
        return Factura.builder()
                .uuid(UUID.randomUUID())
                .numero(Integer.parseInt(facturaDTO.getNumeroFactura().trim()))
                .anio(Integer.parseInt(facturaDTO.getAnio().trim()))
                .enConvenio(getConvenio(facturaDTO.getEnConvenio()))
                .pagoEstado(getPagoEstado(facturaDTO.getPagoEstado()))
                .build();
    }

    @Override
    public FacturaDTO facturaToFacturaDTO(Factura factura) {
        List<CuotaDTO> cuotaDTOList;
        cuotaDTOList = factura.getCuotaList().stream().map(cuotaMapper::cuotaToCuotaDTO).collect(Collectors.toList());
        cuotaDTOList.sort(Comparator.comparing(CuotaDTO::getFechaPagoLocalDate).reversed());
        return FacturaDTO.builder()
                .idFactura(String.valueOf(factura.getUuid()))
                .numeroFactura(String.valueOf(factura.getNumero()))
                .numeroMatriculado(String.valueOf(factura.getMatriculado().getNumeroMatricula()))
                .anio(String.valueOf(factura.getAnio()))
                .enConvenio(getConvenio(factura.getEnConvenio()))
                .pagoEstado(getPagoEstado(factura.getPagoEstado()))
                .cuotas(cuotaDTOList)
                .build();
    }

    private Convenio getConvenio(String convenioString){
        if(!convenioString.isBlank()){
            for (Convenio convenio: Convenio.values()) {
                if (convenio.getConvenio().equalsIgnoreCase(convenioString)) {
                    return convenio;
                }
            }
        }
        return null;
    }

    private String getConvenio(Convenio convenio){
        return convenio.getConvenio();
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
