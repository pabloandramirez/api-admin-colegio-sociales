package ar.com.colegiotrabsociales.administracion.mapper.matriculado;

import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;

public interface MatriculadoMapper {

    Matriculado matriculadoDTOtoMatriculado(MatriculadoDTO matriculadoDTO);

    MatriculadoDTO matriculadoToMatriculadoDTO(Matriculado matriculado);
}
