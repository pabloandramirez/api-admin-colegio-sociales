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
    List<MatriculadoDTO> conseguirMatriculadoPorNumero(Long numero);
    List<MatriculadoDTO> conseguirMatriculadoPorNombreApellido(String nombreApellido);
    List<MatriculadoDTO> conseguirMatriculadoPorDNI(Long dni);

    //PUT
    Optional<MatriculadoDTO> actualizarMatriculado(UUID idMatriculado, MatriculadoDTO matriculadoActualizado);

    //DELETE
    boolean borrarMatriculado(UUID idMatriculado);

}
