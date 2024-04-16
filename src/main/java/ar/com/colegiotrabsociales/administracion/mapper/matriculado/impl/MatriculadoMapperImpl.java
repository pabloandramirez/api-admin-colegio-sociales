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
import java.util.List;
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
                .dni(Integer.parseInt(matriculadoDTO.getDni().trim()))
                .nombresApellidos(matriculadoDTO.getNombresApellidos().trim().toLowerCase())
                .numeroMatricula(Integer.parseInt(matriculadoDTO.getNumeroMatricula().trim()))
                .categoria(getCategoria(matriculadoDTO.getCategoria().trim()));

        if (matriculadoDTO.getCategoria().equalsIgnoreCase(String.valueOf(Categoria.B))) {
            builder.becadoOMonotributista(getBecadoMonotributista(matriculadoDTO.getBecadoOMonotributista()));
        }

        return builder.build();
    }

    @Override
    public MatriculadoDTO matriculadoToMatriculadoDTO(Matriculado matriculado) {
        List<FacturaDTO> facturaDTOList = new java.util.ArrayList<>(matriculado.getFacturas().stream().map(facturaMapper::facturaToFacturaDTO).toList());
        facturaDTOList.sort(Comparator.comparing(FacturaDTO::getAnioLong).reversed());
        MatriculadoDTO.MatriculadoDTOBuilder builder = MatriculadoDTO.builder()
                .idMatriculado(String.valueOf(matriculado.getUuid()))
                .dni(String.valueOf(matriculado.getDni()))
                .nombresApellidos(matriculado.getNombresApellidos())
                .numeroMatricula(String.valueOf(matriculado.getNumeroMatricula()))
                .categoria(getCategoria(matriculado.getCategoria()))
                .facturas(facturaDTOList);

        if(matriculado.getUsuario() != null){
            builder.usuario(matriculado.getUsuario().getUsuario());
        }

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
