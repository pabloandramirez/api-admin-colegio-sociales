package ar.com.colegiotrabsociales.administracion.domain;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Cuota {

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

    private LocalDate fechaVencimiento;

    private PagoEstado pagoEstado;

    @ManyToOne
    private Factura factura;
}
