package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum BecadoMonotributista {
    BECADO("BECADO"),
    MONOTRIBUTISTA("MONOTRIBUTISTA");

    private final String becadoMonotributista;

    BecadoMonotributista(String becadoMonotributista){ this.becadoMonotributista = becadoMonotributista;}
}
