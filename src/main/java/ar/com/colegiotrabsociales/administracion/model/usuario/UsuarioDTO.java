package ar.com.colegiotrabsociales.administracion.model.usuario;

import lombok.*;

import java.util.List;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String uuid;
    private String usuario;
    private String password;
    private Set<String> roles;
}
