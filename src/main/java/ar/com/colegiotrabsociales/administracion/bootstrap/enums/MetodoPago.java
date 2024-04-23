package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum MetodoPago {
    EFECTIVO("EFECTIVO"),
    TRANSFERENCIA("TRANSFERENCIA");

    private final String metodoPago;

    MetodoPago(String metodoPago){ this.metodoPago = metodoPago;}
}
