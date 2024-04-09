package ar.com.colegiotrabsociales.administracion.domain;


import ar.com.colegiotrabsociales.administracion.bootstrap.enums.BecadoMonotributista;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private Long numeroMatricula;

    @Column(length = 50, columnDefinition = "varchar(50)", updatable = true, nullable = false)
    private String nombres;

    @Column(length = 50, columnDefinition = "varchar(50)", updatable = true, nullable = false)
    private String apellido;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Long dni;

    @Column(updatable = true, nullable = false)
    private Categoria categoria;

    @Column(updatable = true, nullable = false)
    private BecadoMonotributista becadoOMonotributista;

    @OneToMany(mappedBy = "matriculado")
    private List<Factura> facturas;
}
