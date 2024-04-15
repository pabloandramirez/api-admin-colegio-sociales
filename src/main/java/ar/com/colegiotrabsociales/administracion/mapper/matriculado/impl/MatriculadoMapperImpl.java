package ar.com.colegiotrabsociales.administracion.mapper.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MatriculadoMapperImpl implements MatriculadoMapper {

    private final FacturaMapper facturaMapper;

    @Override
    public Matriculado matriculadoDTOtoMatriculado(MatriculadoDTO matriculadoDTO) {
        Matriculado.MatriculadoBuilder builder = Matriculado.builder()
                .uuid(UUID.randomUUID())
                .dni(Long.valueOf(matriculadoDTO.getDni()))
                .nombresApellidos(matriculadoDTO.getNombresApellidos().toLowerCase())
                .numeroMatricula(Long.valueOf(matriculadoDTO.getNumeroMatricula()))
                .categoria(getCategoria(matriculadoDTO.getCategoria()));

        if (matriculadoDTO.getBecadoOMonotributista() != null) {
            builder.becadoOMonotributista(getBecadoMonotributista(matriculadoDTO.getBecadoOMonotributista()));
        }

        return builder.build();
    }

    @Override
    public MatriculadoDTO matriculadoToMatriculadoDTO(Matriculado matriculado) {
        MatriculadoDTO.MatriculadoDTOBuilder builder = MatriculadoDTO.builder()
                .idMatriculado(String.valueOf(matriculado.getUuid()))
                .dni(String.valueOf(matriculado.getDni()))
                .nombresApellidos(matriculado.getNombresApellidos())
                .numeroMatricula(String.valueOf(matriculado.getNumeroMatricula()))
                .categoria(getCategoria(matriculado.getCategoria()))
                .facturas(matriculado.getFacturas().stream().map(facturaMapper::facturaToFacturaDTO).collect(Collectors.toList()));

        if (matriculado.getBecadoOMonotributista() != null) {
            builder.becadoOMonotributista(getBecadoMonotributista(matriculado.getBecadoOMonotributista()));
        }


        return builder.build();
    }

    private Categoria getCategoria(String categoriaString){
        if(!categoriaString.isBlank()){
            for (Categoria categoria: Categoria.values()) {
                if (categoria.getCategoria().equalsIgnoreCase(categoriaString)) {
                    return categoria;
                }
            }
        }
        return null;
    }

    private String getCategoria(Categoria categoria){
        return categoria.getCategoria();
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

    private String getBecadoMonotributista(BecadoMonotributista becadoMonotributista){
        return becadoMonotributista.getBecadoMonotributista();
    }
}
