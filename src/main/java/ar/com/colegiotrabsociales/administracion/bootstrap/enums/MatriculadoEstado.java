package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum MatriculadoEstado {
    ACTIVO("ACTIVO"),
    IRREGULAR("IRREGULAR"),
    SUSPENDIDO("SUSPENDIDO");

    private final String estado;

    MatriculadoEstado(String estado){ this.estado = estado;}
}
