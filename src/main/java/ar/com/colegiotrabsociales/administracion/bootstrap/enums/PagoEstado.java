package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum PagoEstado {
    PAGADO("PAGADO"),
    PARCIAL("PAGO PARCIAL"),
    IMPAGO("IMPAGO");

    private final String pagoEstado;

    PagoEstado(String pagoEstado){ this.pagoEstado = pagoEstado;}
}
