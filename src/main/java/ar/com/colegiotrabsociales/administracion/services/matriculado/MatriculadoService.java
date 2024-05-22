package ar.com.colegiotrabsociales.administracion.services.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatriculadoService {

    //POST
    Matriculado crearMatriculado(MatriculadoDTO matriculadoDTO);

    //GET
    List<MatriculadoDTO> conseguirMatriculados();

    Optional<MatriculadoDTO> getMatriculadoUUID(UUID idMatriculado);
    List<MatriculadoDTO> conseguirMatriculadoPorDNI(Integer dni, Integer indiceInicio, Integer matriculadosPorPagina);
    List<MatriculadoDTO> conseguirMatriculadoPorUsuario(String usuario);
    List<MatriculadoDTO> getMatriculadosPaginados(int indiceInicio, int matriculadosPorPagina);
    List<MatriculadoDTO> getMatriculadosPorMatricula(Integer numeroMatricula);

    //PUT
    Optional<MatriculadoDTO> actualizarMatriculado(UUID idMatriculado, MatriculadoDTO matriculadoActualizado);

    //DELETE
    boolean borrarMatriculado(UUID idMatriculado);

}
