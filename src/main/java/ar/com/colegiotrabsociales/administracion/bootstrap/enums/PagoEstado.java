package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum PagoEstado {
    PAGADO("pagado"),
    PARCIAL("pago parcial"),
    IMPAGO("impago");

    private final String pagoEstado;

    PagoEstado(String pagoEstado){ this.pagoEstado = pagoEstado;}
}
