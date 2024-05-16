package ar.com.colegiotrabsociales.administracion.mapper.matriculado.impl;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.MatriculadoEstado;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Role;
import ar.com.colegiotrabsociales.administracion.domain.Cuota;
import ar.com.colegiotrabsociales.administracion.domain.Matriculado;
import ar.com.colegiotrabsociales.administracion.mapper.factura.FacturaMapper;
import ar.com.colegiotrabsociales.administracion.mapper.matriculado.MatriculadoMapper;
import ar.com.colegiotrabsociales.administracion.model.factura.FacturaDTO;
import ar.com.colegiotrabsociales.administracion.model.matriculado.MatriculadoDTO;
import ar.com.colegiotrabsociales.administracion.repository.matriculado.MatriculadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MatriculadoMapperImpl implements MatriculadoMapper {

    private final FacturaMapper facturaMapper;

    private final MatriculadoRepository matriculadoRepository;

    @Override
    public Matriculado matriculadoDTOtoMatriculado(MatriculadoDTO matriculadoDTO) {
        Matriculado.MatriculadoBuilder builder = Matriculado.builder()
                .uuid(UUID.randomUUID())
                .dni(Integer.parseInt(matriculadoDTO.getDni().trim()))
                .nombresApellidos(matriculadoDTO.getNombresApellidos().trim().toLowerCase())
                .numeroMatricula(Integer.parseInt(matriculadoDTO.getNumeroMatricula().trim()))
                .categoria(getCategoria(matriculadoDTO.getCategoria().trim()))
                .matriculadoEstado(getMatriculadoEstado(matriculadoDTO.getMatriculadoEstado()))
                .email(matriculadoDTO.getEmail())
                .telContacto(Integer.valueOf(matriculadoDTO.getTelContacto()))
                .linkLegajo(matriculadoDTO.getLinkLegajo());

        if (matriculadoDTO.getCategoria().equalsIgnoreCase(String.valueOf(Categoria.B))) {
            builder.becadoOMonotributista(getBecadoMonotributista(matriculadoDTO.getBecadoOMonotributista()));
        }

        return builder.build();
    }

    @Override
    public MatriculadoDTO matriculadoToMatriculadoDTO(Matriculado matriculado) {
        List<FacturaDTO> facturaDTOList = new java.util.ArrayList<>(matriculado.getFacturas().stream().map(facturaMapper::facturaToFacturaDTO).toList());
        facturaDTOList.sort(Comparator.comparing(FacturaDTO::getAnio).reversed());
        MatriculadoDTO.MatriculadoDTOBuilder builder = MatriculadoDTO.builder()
                .idMatriculado(String.valueOf(matriculado.getUuid()))
                .dni(String.valueOf(matriculado.getDni()))
                .nombresApellidos(matriculado.getNombresApellidos().toUpperCase())
                .numeroMatriculaInt(matriculado.getNumeroMatricula())
                .numeroMatricula(String.valueOf(matriculado.getNumeroMatricula()))
                .categoria(getCategoria(matriculado.getCategoria()))
                .email(matriculado.getEmail())
                .facturas(facturaDTOList);

        if(matriculado.getUsuario() != null){
            builder.usuario(matriculado.getUsuario().getUsuario());
        }

        if (matriculado.getBecadoOMonotributista() != null) {
            builder.becadoOMonotributista(getBecadoMonotributista(matriculado.getBecadoOMonotributista()));
        }

        if (matriculado.getMatriculadoEstado() != null){
            builder.matriculadoEstado(actualizarMatriculadoEstado(matriculado));
        }

        if(matriculado.getTelContacto() != null){
            builder.telContacto(matriculado.getTelContacto().toString());
        }

        if (matriculado.getEmail() != null){
            builder.email(matriculado.getEmail());
        }

        if (matriculado.getLinkLegajo() != null){
            builder.linkLegajo(matriculado.getLinkLegajo().trim());
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

    private String getMatriculadoEstado(MatriculadoEstado matriculadoEstado){
        return matriculadoEstado.getEstado();
    }

    private String actualizarMatriculadoEstado(Matriculado matriculado){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.minusMonths(3);
        if (!matriculado.getFacturas().isEmpty()){
            if (!matriculado.getFacturas().get(0).getCuotaList().isEmpty()){
                LocalDate ultimaFechaCuotaPaga = matriculado.getFacturas().get(0).getCuotaList().get(0).getFechaPago();
                if (ultimaFechaCuotaPaga == null || ultimaFechaCuotaPaga.isBefore(fechaLimite)){
                    matriculado.setMatriculadoEstado(MatriculadoEstado.IRREGULAR);
                    matriculadoRepository.saveAndFlush(matriculado);
                    return matriculado.getMatriculadoEstado().getEstado();
                } else if(ultimaFechaCuotaPaga.isAfter(fechaLimite)){
                    matriculado.setMatriculadoEstado(MatriculadoEstado.ACTIVO);
                    matriculadoRepository.saveAndFlush(matriculado);
                    return matriculado.getMatriculadoEstado().getEstado();
                } else {
                    return matriculado.getMatriculadoEstado().getEstado();
                }
            }
        }
        return matriculado.getMatriculadoEstado().getEstado();
    }
}
