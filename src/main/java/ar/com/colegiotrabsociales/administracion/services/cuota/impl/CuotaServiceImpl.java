package ar.com.colegiotrabsociales.administracion.services.cuota.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.MetodoPago;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.exceptions.NotFoundException;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.repository.cuota.CuotaRepository;
import ar.com.colegiotrabsociales.administracion.repository.factura.FacturaRepository;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.services.cuota.CuotaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class CuotaServiceImpl implements CuotaService {

    private CuotaMapper cuotaMapper;

    private CuotaRepository cuotaRepository;

    private MatriculadoRepository matriculadoRepository;

    @Override
    public Cuota crearCuota(CuotaDTO cuotaDTO) throws NotFoundException {
        Cuota cuota = cuotaMapper.cuotaDTOtoCuota(cuotaDTO);
        Optional<Matriculado> matriculadoOptional = matriculadoRepository
                .findByNumeroMatricula(Long.valueOf(cuotaDTO.getNumeroMatriculado()));
        if (matriculadoOptional.isPresent()){
            for (Factura factura: matriculadoOptional.get().getFacturas()){
                if (Objects.equals(factura.getAnio(), Integer.valueOf(cuotaDTO.getAnioFactura()))){
                    cuota.setMatriculado(matriculadoOptional.get());
                    cuota.setFactura(factura);
                }
            }
            return cuotaRepository.save(cuota);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<CuotaDTO> verCuotas() {
        List<CuotaDTO> cuotaDTOList = new ArrayList<>();
        for (Cuota cuota: cuotaRepository.findAll()) {
            cuotaDTOList.add(cuotaMapper.cuotaToCuotaDTO(cuota));
        }
        cuotaDTOList.sort(Comparator.comparing(CuotaDTO::getFechaPagoLocalDate).reversed());
        return cuotaDTOList;
    }

    @Override
    public List<CuotaDTO> verCuotasPorDniNumeroMatricula(Long dni, Long numeroMatricula) {
        List<CuotaDTO> cuotaDTOList = new ArrayList<>();
        List<Cuota> cuotas = cuotaRepository.findByMatriculadoDniContainingAndMatriculadoNumeroMatriculaContaining(
                dni != null ? dni : "",
                numeroMatricula != null ? numeroMatricula : "");
        for (Cuota cuota: cuotas){
            cuotaDTOList.add(cuotaMapper.cuotaToCuotaDTO(cuota));
        }
        return cuotaDTOList;
    }

    @Override
    public Optional<CuotaDTO> actualizarCuota(UUID idCuota, CuotaDTO cuotaActualizada) {
        Optional<Cuota> cuotaOptional = cuotaRepository.findById(idCuota);
        if (cuotaOptional.isPresent()){
            actualizacionCuota(cuotaOptional.get(), cuotaActualizada);
            cuotaRepository.saveAndFlush(cuotaOptional.get());
            return Optional.of(cuotaMapper.cuotaToCuotaDTO(cuotaOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean actualizarCuotas(Categoria categoria, BecadoMonotributista becadoMonotributista, Long monto) {
        List<Cuota> cuotaList = cuotaRepository.findAll();
        for (Cuota cuota: cuotaList) {
            if (cuota.getMatriculado().getCategoria()==categoria && cuota.getFactura().getEnConvenio()== Convenio.NO){
                if (cuota.getMatriculado().getCategoria()==Categoria.B){
                    if (cuota.getMatriculado().getBecadoOMonotributista()==becadoMonotributista){
                        cuota.setMonto(Double.valueOf(monto));
                        cuotaRepository.saveAndFlush(cuota);
                    }
                } else{
                    cuota.setMonto(Double.valueOf(monto));
                    cuotaRepository.saveAndFlush(cuota);
                }
            }
        }
        return true;
    }

    @Override
    public boolean borrarCuota(UUID idCuota) {
        if(cuotaRepository.existsById(idCuota)){
            cuotaRepository.deleteById(idCuota);
            return true;
        }
        return false;
    }

    private void actualizacionCuota(Cuota cuota, CuotaDTO cuotaActualizada){
        if (cuotaActualizada.getNumeroComprobante() != null && cuotaActualizada.getNumeroComprobante().isBlank()){
            cuota.setNumeroComprobante(Integer.parseInt(cuotaActualizada.getNumeroComprobante()));
        }

        if (cuotaActualizada.getMonto() != null && cuotaActualizada.getMonto().isBlank()){
            cuota.setMonto(Double.valueOf(cuotaActualizada.getMonto()));
        }

        if (cuotaActualizada.getAnioFactura() != null && !cuotaActualizada.getAnioFactura().isBlank()){
            for (Factura factura: cuota.getMatriculado().getFacturas()){
                if (Objects.equals(factura.getAnio(), Integer.valueOf(cuotaActualizada.getAnioFactura()))){
                    cuota.setFactura(factura);
                }
            }
        }

        if (cuotaActualizada.getNumeroMatriculado() != null && !cuotaActualizada.getNumeroMatriculado().isBlank()){
            Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumeroMatricula(Long.valueOf(cuotaActualizada.getNumeroMatriculado()));
            matriculadoOptional.ifPresent(cuota::setMatriculado);
        }

        if (cuotaActualizada.getLinkComprobante() != null && !cuotaActualizada.getLinkComprobante().isBlank()){
            cuota.setLinkComprobante(cuotaActualizada.getLinkComprobante());
        }

        if (cuotaActualizada.getMetodoPago()!=null && !cuotaActualizada.getMetodoPago().isBlank()){
            cuota.setMetodoPago(getMetodoPago(cuotaActualizada.getMetodoPago()));
        }

        if (cuotaActualizada.getFechaPago()!= null && !cuotaActualizada.getFechaPago().isBlank()){
            cuota.setFechaPago(getLocalDate(cuotaActualizada.getFechaPago()));
        }
    }

    private MetodoPago getMetodoPago(String metodoPagoString){
        if(!metodoPagoString.isBlank()){
            for (MetodoPago metodoPago: MetodoPago.values()) {
                if (metodoPago.getMetodoPago().equalsIgnoreCase(metodoPagoString)) {
                    return metodoPago;
                }
            }
        }
        return null;
    }

    private LocalDate getLocalDate(String localDate){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(localDate, formato);
    }

}
