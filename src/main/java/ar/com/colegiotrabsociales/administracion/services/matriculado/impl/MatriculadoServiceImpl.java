package ar.com.colegiotrabsociales.administracion.services.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
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
    public List<MatriculadoDTO> conseguirMatriculadoPorDNIyNumeroyNombreApellido(Long dni, Long numero, String nombreApellido) {
        List<MatriculadoDTO> matriculadoDTOSList = new ArrayList<>();
        List<Matriculado> matriculados = matriculadoRepository.findByDniContainingAndNumeroMatriculaContainingAndNombresApellidosContaining(
                dni != null ? dni : "",
                numero != null ? numero : "",
                nombreApellido != null ? nombreApellido.toLowerCase().trim() : "");
        for (Matriculado matriculado: matriculados) {
            matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
        }
        /*for (Matriculado matriculado : matriculadoRepository.findAll()) {
            if (dni == null || dni.isBlank()) {
                if (numero == null || numero.isBlank()){
                    if (nombreApellido!=null && !nombreApellido.isBlank()){
                        if (matriculado.getNombresApellidos().contains(nombreApellido.toLowerCase().trim())){
                            matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                        }
                    }
                } else if (nombreApellido==null || nombreApellido.isBlank()) {
                    if (matriculado.getNumeroMatricula().toString().contains(numero.trim())){
                        matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                    }
                } else{
                    if (matriculado.getNombresApellidos().contains(nombreApellido.toLowerCase().trim())
                        && matriculado.getNumeroMatricula().toString().contains(numero.trim())){
                        matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                    }
                }
            } else if (numero == null || numero.isBlank()) {
                if (nombreApellido==null || nombreApellido.isBlank()) {
                    if (matriculado.getDni().toString().contains(dni.trim())){
                        matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                    }
                } else{
                    if (matriculado.getNombresApellidos().contains(nombreApellido.toLowerCase().trim())
                            && matriculado.getDni().toString().contains(dni.trim())){
                        matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                    }
                }
            } else {
                if (matriculado.getDni().toString().contains(dni.trim())
                        && matriculado.getNumeroMatricula().toString().contains(numero.trim())){
                    matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                }
            }

        }*/
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

        if (matriculadoActualizado.getNombresApellidos() != null && matriculadoActualizado.getNombresApellidos().isBlank()){
            matriculado.setNombresApellidos(matriculadoActualizado.getNombresApellidos());
        }

        if (matriculadoActualizado.getNumeroMatricula() != null && !matriculadoActualizado.getNumeroMatricula().isBlank()){
            matriculado.setNumeroMatricula(Long.valueOf(matriculadoActualizado.getNumeroMatricula()));
        }

        if (matriculado.getCategoria() != null && !matriculado.getCategoria().toString().isBlank()){
            matriculado.setCategoria(Categoria.valueOf(matriculadoActualizado.getCategoria()));
        }

        if (matriculadoActualizado.getBecadoOMonotributista() != null && !matriculadoActualizado.getBecadoOMonotributista().isBlank()){
            matriculado.setBecadoOMonotributista(BecadoMonotributista.valueOf(matriculadoActualizado.getBecadoOMonotributista()));
        }
    }
}
