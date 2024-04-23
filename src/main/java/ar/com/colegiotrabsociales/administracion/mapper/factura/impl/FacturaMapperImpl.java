package ar.com.colegiotrabsociales.administracion.mapper.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.repository.cuota.CuotaRepository;
import ar.com.colegiotrabsociales.administracion.repository.factura.FacturaRepository;
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

    private final FacturaRepository facturaRepository;
    @Override
    public Factura facturaDTOtoFactura(FacturaDTO facturaDTO) {
        return Factura.builder()
                .uuid(UUID.randomUUID())
                .numero(Integer.parseInt(facturaDTO.getNumeroFactura().trim()))
                .montoFactura(Double.valueOf(facturaDTO.getMonto()))
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
                .monto(factura.getMontoFactura().toString())
                .anio(String.valueOf(factura.getAnio()))
                .enConvenio(getConvenio(factura.getEnConvenio()))
                .pagoEstado(cambiarPagoEstado(factura))
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

    private String cambiarPagoEstado(Factura factura){
        Double totalPagado = 0.0;
        List<Cuota> cuotaList = factura.getCuotaList();
        if (!cuotaList.isEmpty()){
            for (Cuota cuota: cuotaList){
                totalPagado += cuota.getMonto();
            }
        }
        if (totalPagado.equals(factura.getMontoFactura())){
            factura.setPagoEstado(PagoEstado.PAGADO);
            facturaRepository.saveAndFlush(factura);
            return factura.getPagoEstado().getPagoEstado();
        } else if (totalPagado>0.0 && totalPagado<factura.getMontoFactura()) {
            factura.setPagoEstado(PagoEstado.PARCIAL);
            facturaRepository.saveAndFlush(factura);
            return factura.getPagoEstado().getPagoEstado();
        } else {
            return factura.getPagoEstado().getPagoEstado();
        }
    }
}
