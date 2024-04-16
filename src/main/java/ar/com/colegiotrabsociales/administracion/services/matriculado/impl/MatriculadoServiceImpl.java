package ar.com.colegiotrabsociales.administracion.services.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.domain.Usuario;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import ar.com.colegiotrabsociales.administracion.repository.usuario.UsuarioRepository;
import ar.com.colegiotrabsociales.administracion.services.matriculado.MatriculadoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MatriculadoServiceImpl implements MatriculadoService {

    private final MatriculadoMapper matriculadoMapper;

    private final MatriculadoRepository matriculadoRepository;

    private final UsuarioRepository usuarioRepository;

    @Override
    public Matriculado crearMatriculado(MatriculadoDTO matriculadoDTO) {
        Matriculado matriculado = matriculadoMapper.matriculadoDTOtoMatriculado(matriculadoDTO);
        if (matriculadoDTO.getUsuario() != null && !matriculadoDTO.getUsuario().isBlank()){
            Optional<Usuario> usuarioOptional = usuarioRepository.findByUsuario(matriculadoDTO.getUsuario());
            usuarioOptional.ifPresent(matriculado::setUsuario);
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
                String.valueOf(dni != null ? dni : ""),
                String.valueOf(numero != null ? numero : ""),
                nombreApellido != null ? nombreApellido.toLowerCase().trim() : ""
        );
        for (Matriculado matriculado: matriculados) {
            matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
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
            matriculado.setDni(Integer.valueOf(matriculadoActualizado.getDni().trim()));
        }

        if (matriculadoActualizado.getNombresApellidos() != null && matriculadoActualizado.getNombresApellidos().isBlank()){
            matriculado.setNombresApellidos(matriculadoActualizado.getNombresApellidos());
        }

        if (matriculadoActualizado.getNumeroMatricula() != null && !matriculadoActualizado.getNumeroMatricula().isBlank()){
            matriculado.setNumeroMatricula(Integer.parseInt(matriculadoActualizado.getNumeroMatricula()));
        }

        if (matriculado.getCategoria() != null && !matriculado.getCategoria().toString().isBlank()){
            matriculado.setCategoria(Categoria.valueOf(matriculadoActualizado.getCategoria()));
        }

        if (matriculadoActualizado.getBecadoOMonotributista() != null && !matriculadoActualizado.getBecadoOMonotributista().isBlank()){
            matriculado.setBecadoOMonotributista(getBecadoMonotributista(matriculadoActualizado.getBecadoOMonotributista()));
        }
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
