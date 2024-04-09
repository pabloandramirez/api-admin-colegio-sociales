package ar.com.colegiotrabsociales.administracion.mapper.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.services.matriculado.MatriculadoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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
                .numero(Long.valueOf(facturaDTO.getNumeroFactura()))
                .enConvenio(getConvenio(facturaDTO.getEnConvenio()))
                .build();
    }

    @Override
    public FacturaDTO facturaToFacturaDTO(Factura factura) {
        return FacturaDTO.builder()
                .idFactura(String.valueOf(factura.getUuid()))
                .numeroFactura(String.valueOf(factura.getNumero()))
                .numeroMatriculado(String.valueOf(factura.getMatriculado().getNumeroMatricula()))
                .enConvenio(getConvenio(factura.getEnConvenio()))
                .cuotas(factura.getCuotaList().stream().map(cuota ->
                        cuotaMapper.cuotaToCuotaDTO(cuota).toString()).collect(Collectors.toList()))
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
}
