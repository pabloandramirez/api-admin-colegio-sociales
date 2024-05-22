package ar.com.colegiotrabsociales.administracion.services.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.MatriculadoEstado;
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
    public Optional<MatriculadoDTO> getMatriculadoUUID(UUID idMatriculado) {
        Optional<Matriculado> optionalMatriculado = matriculadoRepository.findById(idMatriculado);
        return optionalMatriculado.map(matriculadoMapper::matriculadoToMatriculadoDTO);
    }

    @Override
    public List<MatriculadoDTO> conseguirMatriculadoPorDNI(Integer dni, Integer indiceInicio, Integer matriculadosPorPagina) {

        List<MatriculadoDTO> matriculadoDTOSList = new ArrayList<>();

        for (Matriculado matriculado : matriculadoRepository.findAll()) {
            boolean matchDni = dni == null || matriculado.getDni().toString().contains(dni.toString());

            // Si hay solo un filtro, o si todos los filtros coinciden, agregamos el DTO
            if (dni == null || matchDni) {
                matriculadoDTOSList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
            }
        }

        matriculadoDTOSList.sort(Comparator.comparing(MatriculadoDTO::getNumeroMatriculaInt));

        // Calcular el índice final de las noticias en función de la página y la cantidad de noticias por página
        int indiceFinal = Math.min(indiceInicio + matriculadosPorPagina, matriculadoDTOSList.size());

        // Devolver las noticias correspondientes a la página solicitada
        return matriculadoDTOSList.subList(indiceInicio, indiceFinal);
    }

    @Override
    public List<MatriculadoDTO> conseguirMatriculadoPorUsuario(String usuario) {
        List<MatriculadoDTO> matriculadoDTOList = new ArrayList<>();
        for (Matriculado matriculado:matriculadoRepository.findAll()){
            if (matriculado.getUsuario()!=null){
                if (matriculado.getUsuario().getUsuario().equalsIgnoreCase(usuario)){
                    matriculadoDTOList.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
                }
            }
        }
        return matriculadoDTOList;
    }

    @Override
    public List<MatriculadoDTO> getMatriculadosPaginados(int indiceInicio, int matriculadosPorPagina) {
        // Obtener todas las noticias desde la base de datos
        List<MatriculadoDTO> todosLosMatriculados = new ArrayList<>(); // Suponiendo que utilizas Spring Data JPA

        for (Matriculado matriculado: matriculadoRepository.findAll()) {
            todosLosMatriculados.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
        }

        todosLosMatriculados.sort(Comparator.comparing(MatriculadoDTO::getNumeroMatriculaInt));

        // Calcular el índice final de las noticias en función de la página y la cantidad de noticias por página
        int indiceFinal = Math.min(indiceInicio + matriculadosPorPagina, todosLosMatriculados.size());

        // Devolver las noticias correspondientes a la página solicitada
        return todosLosMatriculados.subList(indiceInicio, indiceFinal);
    }

    @Override
    public List<MatriculadoDTO> getMatriculadosPorMatricula(Integer numeroMatricula) {
        List<MatriculadoDTO> matriculadoDTOS = new ArrayList<>();
        for (Matriculado matriculado: matriculadoRepository.findAll()){
            if (matriculado.getNumeroMatricula().equals(numeroMatricula)){
                matriculadoDTOS.add(matriculadoMapper.matriculadoToMatriculadoDTO(matriculado));
            }
        }
        return matriculadoDTOS;
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
            matriculado.setDni(Integer.parseInt(matriculadoActualizado.getDni().trim()));
        }

        if (matriculadoActualizado.getNombres() != null && matriculadoActualizado.getNombres().isBlank()){
            matriculado.setNombres(matriculadoActualizado.getNombres());
        }

        if (matriculadoActualizado.getApellidos() != null && matriculadoActualizado.getApellidos().isBlank()){
            matriculado.setApellidos(matriculadoActualizado.getApellidos());
        }

        if (matriculadoActualizado.getNumeroMatricula() != null && !matriculadoActualizado.getNumeroMatricula().isBlank()){
            matriculado.setNumeroMatricula(Integer.parseInt(matriculadoActualizado.getNumeroMatricula()));
        }

        if (matriculadoActualizado.getCategoria() != null && !matriculadoActualizado.getCategoria().isBlank()){
            matriculado.setCategoria(Categoria.valueOf(matriculadoActualizado.getCategoria()));
        }

        if (matriculadoActualizado.getBecadoOMonotributista() != null && !matriculadoActualizado.getBecadoOMonotributista().isBlank()){
            matriculado.setBecadoOMonotributista(getBecadoMonotributista(matriculadoActualizado.getBecadoOMonotributista()));
        }

        if(matriculadoActualizado.getMatriculadoEstado() != null && !matriculadoActualizado.getMatriculadoEstado().isBlank()) {
            matriculado.setMatriculadoEstado(getMatriculadoEstado(matriculadoActualizado.getMatriculadoEstado()));
        }

        if (matriculadoActualizado.getEmail() != null && !matriculadoActualizado.getEmail().isBlank()){
            matriculado.setEmail(matriculadoActualizado.getEmail());
        }

        if (matriculadoActualizado.getTelContacto() != null && !matriculadoActualizado.getTelContacto().isBlank()){
            matriculado.setTelContacto(Long.valueOf(matriculadoActualizado.getTelContacto()));
        }

        if (!matriculadoActualizado.getLinkLegajo().isBlank()){
            matriculado.setLinkLegajo(matriculadoActualizado.getLinkLegajo());
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

    private MatriculadoEstado getMatriculadoEstado(String estadoMatriculadoString){
        if(!estadoMatriculadoString.isBlank()){
            for (MatriculadoEstado matriculadoEstado: MatriculadoEstado.values()) {
                if (matriculadoEstado.getEstado().equalsIgnoreCase(estadoMatriculadoString)) {
                    return matriculadoEstado;
                }
            }
        }
        return null;
    }

}
