package ar.com.colegiotrabsociales.administracion.mapper.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MatriculadoMapperImpl implements MatriculadoMapper {

    private final FacturaMapper facturaMapper;

    @Override
    public Matriculado matriculadoDTOtoMatriculado(MatriculadoDTO matriculadoDTO) {
        return Matriculado.builder()
                .uuid(UUID.randomUUID())
                .dni(Long.valueOf(matriculadoDTO.getDni()))
                .nombres(matriculadoDTO.getNombres())
                .apellido(matriculadoDTO.getApellido())
                .numeroMatricula(Long.valueOf(matriculadoDTO.getNumeroMatricula()))
                .categoria(getCategoria(matriculadoDTO.getCategoria()))
                .becadoOMonotributista(matriculadoDTO.getBecadoOMonotributista())
                .build();
    }

    @Override
    public MatriculadoDTO matriculadoToMatriculadoDTO(Matriculado matriculado) {
        return MatriculadoDTO.builder()
                .idMatriculado(String.valueOf(matriculado.getUuid()))
                .dni(String.valueOf(matriculado.getDni()))
                .nombres(matriculado.getNombres())
                .apellido(matriculado.getApellido())
                .numeroMatricula(String.valueOf(matriculado.getNumeroMatricula()))
                .categoria(getCategoria(matriculado.getCategoria()))
                .becadoOMonotributista(matriculado.getBecadoOMonotributista())
                .facturas(matriculado.getFacturas().stream().map(factura ->
                        facturaMapper.facturaToFacturaDTO(factura).toString()).collect(Collectors.toList()))
                .build();
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
}
