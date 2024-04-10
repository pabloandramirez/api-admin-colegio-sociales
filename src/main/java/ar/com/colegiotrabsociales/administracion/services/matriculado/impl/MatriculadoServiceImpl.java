package ar.com.colegiotrabsociales.administracion.services.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.domain.Factura;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.services.matriculado.MatriculadoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MatriculadoServiceImpl implements MatriculadoService {

    private final MatriculadoMapper matriculadoMapper;

    private final MatriculadoRepository matriculadoRepository;

    @Override
    public Matriculado crearMatriculado(MatriculadoDTO matriculadoDTO) {
        Matriculado matriculado = matriculadoMapper.matriculadoDTOtoMatriculado(matriculadoDTO);
        if (matriculado.getCategoria() == Categoria.B){
            matriculado.setBecadoOMonotributista(BecadoMonotributista.valueOf(matriculadoDTO.getBecadoOMonotributista()));
        }
        return matriculadoRepository.save(matriculado);
    }

    @Override
    public List<MatriculadoDTO> conseguirMatriculados() {
        List<MatriculadoDTO> matriculadoDTOSList = new ArrayList<>();
        for (Matriculado matriculado:
             matriculadoRepository.findAll()) {
            matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
        }
        matriculadoDTOSList.sort(Comparator.comparing(MatriculadoDTO::getNumeroMatricula));
        return matriculadoDTOSList;
    }

    @Override
    public List<MatriculadoDTO> conseguirMatriculadoPorDNIyNumeroyNombreApellido(String dni, String numero, String nombreApellido) {
        List<MatriculadoDTO> matriculadoDTOSList = new ArrayList<>();
        for (Matriculado matriculado : matriculadoRepository.findAll()) {
            if (dni == null || dni.isBlank() && numero == null || numero.isBlank()) {
                if (matriculado.getNombresApellidos().contains(nombreApellido.toLowerCase().trim())){

                }
        }
        return matriculadoDTOSList;
    }

    @Override
    public Optional<MatriculadoDTO> actualizarMatriculado(UUID idMatriculado, MatriculadoDTO matriculadoActualizado) {
        Optional<Matriculado> matriculadoOptional = matriculadoRepository.findById(idMatriculado);
        if(matriculadoOptional.isPresent()){
            actualizacionMatriculado(matriculadoOptional.get(), matriculadoActualizado);
            matriculadoRepository.saveAndFlush(matriculadoOptional.get());
            return Optional.of(matriculadoMapper.matriculadoToMatriculadoDTO(matriculadoOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean borrarMatriculado(UUID idMatriculado) {
        if (matriculadoRepository.existsById(idMatriculado)){
            matriculadoRepository.deleteById(idMatriculado);
            return true;
        }
        return false;
    }

    private void actualizacionMatriculado(Matriculado matriculado, MatriculadoDTO matriculadoActualizado){
        if (matriculadoActualizado.getDni() != null && matriculadoActualizado.getDni().isBlank()){
            matriculado.setDni(Long.valueOf(matriculadoActualizado.getDni().trim()));
        }

        if (matriculadoActualizado.getNombres() != null && matriculadoActualizado.getNombres().isBlank()){
            matriculado.setNombres(matriculadoActualizado.getNombres());
        }

        if (matriculadoActualizado.getNumeroMatricula() != null && !matriculadoActualizado.getNumeroMatricula().isBlank()){
            matriculado.setNumeroMatricula(Long.valueOf(matriculadoActualizado.getNumeroMatricula()));
        }

        if (matriculadoActualizado.getApellido() != null && !matriculadoActualizado.getApellido().isBlank()){
            matriculado.setApellido(matriculadoActualizado.getApellido());
        }

        if (matriculado.getCategoria() != null && !matriculado.getCategoria().toString().isBlank()){
            matriculado.setCategoria(Categoria.valueOf(matriculadoActualizado.getCategoria()));
        }

        if (matriculadoActualizado.getBecadoOMonotributista() != null && !matriculadoActualizado.getBecadoOMonotributista().isBlank()){
            matriculado.setBecadoOMonotributista(BecadoMonotributista.valueOf(matriculadoActualizado.getBecadoOMonotributista()));
        }
    }
}
