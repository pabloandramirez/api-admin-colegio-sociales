package ar.com.colegiotrabsociales.administracion.services.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.repository.factura.FacturaRepository;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.services.factura.FacturaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacturaServiceImpl implements FacturaService {

    private FacturaMapper facturaMapper;

    private FacturaRepository facturaRepository;

    private MatriculadoRepository matriculadoRepository;

    @Override
    public Factura crearFactura(FacturaDTO facturaDTO) {
        Factura factura = facturaMapper.facturaDTOtoFactura(facturaDTO);
        return facturaRepository.save(factura);
    }

    @Override
    public List<FacturaDTO> verFacturas() {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura: facturaRepository.findAll()) {
            facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
        }
        facturaDTOList.sort(Comparator.comparing(FacturaDTO::getNumeroFactura).reversed());
        return facturaDTOList;
    }

    @Override
    public Optional<FacturaDTO> actualizarFactura(UUID idFactura, FacturaDTO facturaActualizada) {
        Optional<Factura> facturaOptional = facturaRepository.findById(idFactura);
        if (facturaOptional.isPresent()){
            actualizacionFactura(facturaOptional.get(), facturaActualizada);
            facturaRepository.saveAndFlush(facturaOptional.get());
            return Optional.of(facturaMapper.facturaToFacturaDTO(facturaOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean borrarFactura(UUID idFactura) {
        if (facturaRepository.existsById(idFactura)){
            facturaRepository.deleteById(idFactura);
            return true;
        }
        return false;
    }

    private void actualizacionFactura(Factura factura, FacturaDTO facturaActualizada){
        if (facturaActualizada.getNumeroFactura() != null && facturaActualizada.getNumeroFactura().isBlank()){
            factura.setNumero(Long.valueOf(facturaActualizada.getNumeroFactura()));
        }

        if (facturaActualizada.getEnConvenio() != null && facturaActualizada.getEnConvenio().isBlank()){
            factura.setEnConvenio(Boolean.getBoolean(facturaActualizada.getEnConvenio().toLowerCase()));
        }

        if (facturaActualizada.getNumeroMatriculado() != null && !facturaActualizada.getNumeroMatriculado().isBlank()){
            Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumero(Long.valueOf(facturaActualizada.getNumeroMatriculado()));
            matriculadoOptional.ifPresent(factura::setMatriculado);
        }
    }

}
