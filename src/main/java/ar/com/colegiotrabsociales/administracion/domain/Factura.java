package ar.com.colegiotrabsociales.administracion.domain;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Factura {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID uuid;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Long numero;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Long monto;

    @Column(length = 5, columnDefinition = "varchar(5)", updatable = true, nullable = false)
    private boolean enConvenio;

    @ManyToOne
    @JoinColumn(name = "matriculado_numero")
    private Matriculado matriculado;

    @OneToMany(mappedBy = "factura")
    private List<Cuota> cuotaList;
}