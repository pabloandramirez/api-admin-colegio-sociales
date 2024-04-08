package ar.com.colegiotrabsociales.administracion.bootstrap.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USUARIO"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role){ this.role = role;}
}
