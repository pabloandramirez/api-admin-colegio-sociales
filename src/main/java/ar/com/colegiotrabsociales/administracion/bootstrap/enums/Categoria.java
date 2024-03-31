package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum Categoria {
    A("A"),
    B("B"),
    C("C");

    private final String categoria;

    Categoria(String categoria){ this.categoria = categoria;}
}
