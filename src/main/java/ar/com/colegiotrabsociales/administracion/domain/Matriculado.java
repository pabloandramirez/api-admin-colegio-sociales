package ar.com.colegiotrabsociales.administracion.domain;


import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.MatriculadoEstado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Matriculado {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID uuid;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Integer numeroMatricula;

    @Column(length = 150, columnDefinition = "varchar(150)", updatable = true, nullable = false)
    private String nombresApellidos;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Integer dni;

    @Column(name = "categoria", updatable = true, nullable = false, length = 36)
    private Categoria categoria;

    @Column(name = "becado_monotributista", updatable = true, nullable = true, length = 36)
    private BecadoMonotributista becadoOMonotributista;

    @Column(name = "estado_matriculado", updatable = true, nullable = true, length = 36)
    private MatriculadoEstado matriculadoEstado;

    @OneToMany(mappedBy = "matriculado")
    private List<Factura> facturas = new ArrayList<>();

    @OneToMany(mappedBy = "matriculado")
    private List<Cuota> cuotas = new ArrayList<>();

    @Column(name = "link_legajo", nullable = true)
    private String linkLegajo;

    @OneToOne(mappedBy = "matriculado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(length = 150, columnDefinition = "varchar(150)", updatable = true, nullable = true)
    private String email;

    public List<Factura> getFacturas(){
        List<Factura> facturas = this.facturas;
        facturas.sort(Comparator.comparing(Factura::getAnio).reversed());
        return facturas;
    }

}
