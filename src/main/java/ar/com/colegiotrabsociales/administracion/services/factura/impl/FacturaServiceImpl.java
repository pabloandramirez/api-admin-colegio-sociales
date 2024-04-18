package ar.com.colegiotrabsociales.administracion.services.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.repository.factura.FacturaRepository;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.services.factura.FacturaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class FacturaServiceImpl implements FacturaService {

    private FacturaMapper facturaMapper;

    private FacturaRepository facturaRepository;

    private MatriculadoRepository matriculadoRepository;

    @Override
    public Factura crearFactura(FacturaDTO facturaDTO) throws NotFoundException {
        Factura factura = facturaMapper.facturaDTOtoFactura(facturaDTO);
        Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumeroMatricula(Long.valueOf(facturaDTO.getNumeroMatriculado()));
        if (matriculadoOptional.isPresent()){
            factura.setMatriculado(matriculadoOptional.get());
            return facturaRepository.save(factura);
        } else {
            log.warn("No se encontro el matriculado");
            throw new NotFoundException();
        }
    }

    @Override
    public List<FacturaDTO> verFacturas() {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura: facturaRepository.findAll()) {
            facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
        }
        facturaDTOList.sort(Comparator.comparing(FacturaDTO::getAnio).reversed());
        return facturaDTOList;
    }

    @Override
    public List<FacturaDTO> verFacturasPorDNIoNumeroMatricula(Long dni, Long numeroMatricula) {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        List<Factura> facturas = facturaRepository.findByMatriculadoDniContainingAndMatriculadoNumeroMatriculaContaining(
                dni != null ? dni : "",
                numeroMatricula != null ? numeroMatricula : "");
        for (Factura factura: facturas) {
            facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
        }
        /*for (Factura factura: facturaRepository.findAll()) {
            if (dni==null || dni.toString().isBlank()){
                if (factura.getMatriculado().getNumeroMatricula().toString().contains(numeroMatricula.toString())){
                    facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
                }
            } else if (numeroMatricula==null || numeroMatricula.toString().isBlank()){
                if (factura.getMatriculado().getDni().toString().contains(dni.toString())){
                    facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
                }
            } else{
                if (factura.getMatriculado().getDni().toString().contains(dni.toString()) &&
                        factura.getMatriculado().getNumeroMatricula().toString().contains(numeroMatricula.toString())){
                    facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
                }
            }
        }*/
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
            factura.setNumero(Integer.parseInt(facturaActualizada.getNumeroFactura().trim()));
        }

        if (facturaActualizada.getEnConvenio() != null && facturaActualizada.getEnConvenio().isBlank()){
            factura.setEnConvenio(getConvenio(facturaActualizada.getEnConvenio()));
        }

        if (facturaActualizada.getNumeroMatriculado() != null && !facturaActualizada.getNumeroMatriculado().isBlank()){
            Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumeroMatricula(Long.valueOf(facturaActualizada.getNumeroMatriculado().trim()));
            matriculadoOptional.ifPresent(factura::setMatriculado);
        }
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

}
