package ar.com.colegiotrabsociales.administracion.model.matriculado;

import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
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
    private Integer numeroMatriculaInt;
    private String nombres;
    private String apellidos;
    private String dni;
    private String categoria;
    private String becadoOMonotributista;
    private String matriculadoEstado;
    private List<FacturaDTO> facturas;
    private String usuario;
    private String linkLegajo;
    private String email;
    private String telContacto;
}


