package ar.com.colegiotrabsociales.administracion.domain;

import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Categoria;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.Convenio;
import ar.com.colegiotrabsociales.administracion.bootstrap.enums.PagoEstado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Comparator;
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

    @Column(columnDefinition = "DOUBLE PRECISION", updatable = true, nullable = false)
    private Double montoFactura;

    @Column(columnDefinition = "DOUBLE PRECISION", updatable = true, nullable = true)
    private Double saldo;

    @Column(columnDefinition = "BIGINT", updatable = true, nullable = false)
    private Integer anio;

    @Column(name = "pago_estado", nullable = false, length = 36)
    private PagoEstado pagoEstado;

    @Column(name = "en_convenio", nullable = false, length = 36)
    private Convenio enConvenio;

    @ManyToOne
    @JoinColumn(name = "matriculado_uuid", referencedColumnName = "uuid")
    private Matriculado matriculado;

    @OneToMany(mappedBy = "factura")
    private List<Cuota> cuotaList;

    public List<Cuota> getCuotaList(){
        List<Cuota> cuotas = this.cuotaList;
        cuotas.sort(Comparator.comparing(Cuota::getFechaPago).reversed());
        return cuotas;
    }
}