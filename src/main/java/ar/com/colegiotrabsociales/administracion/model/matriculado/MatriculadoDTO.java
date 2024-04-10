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
    private String numeroMatricula;
    private String nombresApellidos;
    private String dni;
    private String categoria;
    private String becadoOMonotributista;
    private List<String> facturas;
}


