package ar.com.colegiotrabsociales.administracion.model.matriculado;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculadoDTO {
    private String idMatriculado;
    private Long numeroMatricula;
    private String nombres;
    private String apellido;
    private String dni;
    private String caategoria;
    private String becadoOMonotributista;
    private List<String> facturas;
}


