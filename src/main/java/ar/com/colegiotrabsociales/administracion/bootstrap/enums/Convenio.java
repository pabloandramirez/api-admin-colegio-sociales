package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum Convenio {
    SI("SI"),
    NO("NO");

    private final String convenio;

    Convenio(String convenio){ this.convenio = convenio;}
}
