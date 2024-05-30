package ar.com.colegiotrabsociales.administracion.services.factura.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
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
    public List<Factura> crearFacturas(FacturaDTO facturaDTO, String categoria, String becadoMono)
            throws NotFoundException {
        List<Matriculado> matriculadoList = matriculadoRepository.findAll();
        List<Factura> facturas = new ArrayList<>();
        for (Matriculado matriculado:
             matriculadoList) {
            if (matriculado.getCategoria().getCategoria().equals(categoria.toUpperCase())){
                if (matriculado.getBecadoOMonotributista().getBecadoMonotributista().equalsIgnoreCase(becadoMono)){
                    facturaDTO.setNumeroMatriculado(matriculado.getNumeroMatricula().toString());
                    Factura factura = crearFactura(facturaDTO);
                    facturas.add(factura);
                }
            }
        }
        return facturas;
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
    public List<FacturaDTO> verFacturasPorDNIoNumeroMatricula(Integer dni, Integer numeroMatricula, Integer indiceInicio, Integer facturasPorPagina) {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura : facturaRepository.findAll()) {
            boolean matchDni = dni == null || factura.getMatriculado().getDni().toString().contains(dni.toString());
            boolean matchMatricula = numeroMatricula == null || factura.getMatriculado().getNumeroMatricula().toString().contains(numeroMatricula.toString());

            // Si hay solo un filtro, o si todos los filtros coinciden, agregamos el DTO
            if ((dni == null || matchDni) && (numeroMatricula==null || matchMatricula)) {
                facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
            }
        }

        facturaDTOList.sort(Comparator.comparing(FacturaDTO::getNumeroMatriculadoInt));

        // Calcular el índice final de las noticias en función de la página y la cantidad de noticias por página
        int indiceFinal = Math.min(indiceInicio + facturasPorPagina, facturaDTOList.size());

        // Devolver las noticias correspondientes a la página solicitada
        return facturaDTOList.subList(indiceInicio, indiceFinal);
    }

    @Override
    public List<FacturaDTO> verFacturasPorCategoria(String categoria, String becadoMonotributista, Integer anio) {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura: facturaRepository.findAll()){
            if (factura.getMatriculado().getCategoria()==getCategoria(categoria) &&
                    factura.getMatriculado().getBecadoOMonotributista()==getBecadoMonotributista(becadoMonotributista) &&
                    Objects.equals(factura.getAnio(), anio)){
                facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
            }
        }
        return facturaDTOList;
    }

    @Override
    public Optional<FacturaDTO> actualizarFactura(UUID idFactura, FacturaDTO facturaActualizada) {
        Optional<Factura> facturaOptional = facturaRepository.findById(idFactura);
        if (facturaOptional.isPresent() && !(facturaOptional.get().getPagoEstado()==PagoEstado.PAGADO)){
            actualizacionFactura(facturaOptional.get(), facturaActualizada);
            facturaRepository.saveAndFlush(facturaOptional.get());
            return Optional.of(facturaMapper.facturaToFacturaDTO(facturaOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<FacturaDTO> actualizarFacturas(FacturaDTO facturaDTO,
                                               String categoria,
                                               String becadoMono,
                                               Integer anio) {
        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura: facturaRepository.findAll()){
            if (factura.getMatriculado().getCategoria()==getCategoria(categoria) &&
            factura.getMatriculado().getBecadoOMonotributista()==getBecadoMonotributista(becadoMono) &&
                    Objects.equals(factura.getAnio(), anio) &&
                    !(factura.getPagoEstado()==PagoEstado.PAGADO)){
                actualizacionFactura(factura, facturaDTO);
                facturaRepository.saveAndFlush(factura);
                facturaDTOList.add(facturaMapper.facturaToFacturaDTO(factura));
            }
        }
        return facturaDTOList;
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

        if (facturaActualizada.getEnConvenio() != null && facturaActualizada.getEnConvenio().isBlank()){
            factura.setEnConvenio(getConvenio(facturaActualizada.getEnConvenio()));
        }

        if (facturaActualizada.getNumeroMatriculado() != null && !facturaActualizada.getNumeroMatriculado().isBlank()){
            Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumeroMatricula(Long.valueOf(facturaActualizada.getNumeroMatriculado().trim()));
            matriculadoOptional.ifPresent(factura::setMatriculado);
        }

        if (facturaActualizada.getMonto()!= null && !facturaActualizada.getMonto().isBlank()){
            factura.setMontoFactura(Double.valueOf(facturaActualizada.getMonto()));
        }

        if (facturaActualizada.getAnio()!=null && !facturaActualizada.getAnio().isBlank()){
            factura.setAnio(Integer.valueOf(facturaActualizada.getAnio()));
        }

        if (facturaActualizada.getPagoEstado()!=null && !facturaActualizada.getPagoEstado().isBlank()){
            factura.setPagoEstado(getPagoEstado(facturaActualizada.getPagoEstado()));
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

    private Categoria getCategoria(String categoriaString){
        if(!categoriaString.isBlank()){
            for (Categoria categoria: Categoria.values()) {
                if (categoria.getCategoria().equalsIgnoreCase(categoriaString)) {
                    return categoria;
                }
            }
        }
        return null;
    }

    private BecadoMonotributista getBecadoMonotributista(String becadoMonotributistaString){
        if(!becadoMonotributistaString.isBlank()){
            for (BecadoMonotributista becadoMonotributista: BecadoMonotributista.values()) {
                if (becadoMonotributista.getBecadoMonotributista().equalsIgnoreCase(becadoMonotributistaString)) {
                    return becadoMonotributista;
                }
            }
        }
        return null;
    }

}
