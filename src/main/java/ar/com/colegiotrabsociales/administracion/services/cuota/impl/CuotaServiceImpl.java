package ar.com.colegiotrabsociales.administracion.services.cuota.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.cuota.CuotaMapper;
import ar.com.colegiotrabsociales.administracion.model.cuota.CuotaDTO;
import ar.com.colegiotrabsociales.administracion.repository.cuota.CuotaRepository;
import ar.com.colegiotrabsociales.administracion.repository.factura.FacturaRepository;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.services.cuota.CuotaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class CuotaServiceImpl implements CuotaService {

    private CuotaMapper cuotaMapper;

    private CuotaRepository cuotaRepository;

    private FacturaRepository facturaRepository;

    private MatriculadoRepository matriculadoRepository;

    @Override
    public Cuota crearCuota(CuotaDTO cuotaDTO) {
        Cuota cuota = cuotaMapper.cuotaDTOtoCuota(cuotaDTO);
        return cuotaRepository.save(cuota);
    }

    @Override
    public List<CuotaDTO> verCuotas() {
        List<CuotaDTO> cuotaDTOList = new ArrayList<>();
        for (Cuota cuota: cuotaRepository.findAll()) {
            cuotaDTOList.add(cuotaMapper.cuotaToCuotaDTO(cuota));
        }
        cuotaDTOList.sort(Comparator.comparing(CuotaDTO::getFechaVencimientoDate).reversed());
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
    public boolean borrarCuota(UUID idCuota) {
        if(cuotaRepository.existsById(idCuota)){
            cuotaRepository.deleteById(idCuota);
            return true;
        }
        return false;
    }

    private void actualizacionCuota(Cuota cuota, CuotaDTO cuotaActualizada){
        if (cuotaActualizada.getNumeroCuota() != null && cuotaActualizada.getNumeroCuota().isBlank()){
            cuota.setNumero(Long.valueOf(cuotaActualizada.getNumeroCuota()));
        }

        if (cuotaActualizada.getMonto() != null && cuotaActualizada.getMonto().isBlank()){
            cuota.setMonto(Long.valueOf(cuotaActualizada.getMonto()));
        }

        if (cuotaActualizada.getFechaVencimientoString() != null && !cuotaActualizada.getFechaVencimientoString().isBlank()){
            cuota.setFechaVencimiento(getLocalDate(cuotaActualizada.getFechaVencimientoString()));
        }

        if (cuotaActualizada.getPagoEstado() != null && !cuotaActualizada.getPagoEstado().isEmpty()){
            cuota.setPagoEstado(PagoEstado.valueOf(cuotaActualizada.getPagoEstado()));
        }

        if (cuotaActualizada.getIdFactura() != null && !cuotaActualizada.getIdFactura().isBlank()){
            Optional<Factura> facturaOptional = facturaRepository.findByNumero(Long.valueOf(cuotaActualizada.getIdFactura()));
            facturaOptional.ifPresent(cuota::setFactura);
        }

        if (cuotaActualizada.getIdMatriculado() != null && !cuotaActualizada.getIdMatriculado().isBlank()){
            Optional<Matriculado> matriculadoOptional = matriculadoRepository.findByNumero(Long.valueOf(cuotaActualizada.getIdMatriculado()));
            matriculadoOptional.ifPresent(cuota::setMatriculado);
        }
    }

    private LocalDate getLocalDate(String localDate){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(localDate, formato);
    }

}
