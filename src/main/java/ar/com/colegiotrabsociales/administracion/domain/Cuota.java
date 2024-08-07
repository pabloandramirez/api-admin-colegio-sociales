package ar.com.colegiotrabsociales.administracion.domain;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.MetodoPago;
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
    private Integer numeroComprobante;

    @Column(columnDefinition = "DOUBLE PRECISION", updatable = true, nullable = false)
    private Double monto;

    private LocalDate fechaPago;

    @Column(name = "metodo_pago", nullable = false, length = 36)
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "factura_uuid", referencedColumnName = "uuid")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "matriculado_uuid", referencedColumnName = "uuid")
    private Matriculado matriculado;

    @Column(name = "link_comprobante", nullable = true)
    private String linkComprobante;
}
