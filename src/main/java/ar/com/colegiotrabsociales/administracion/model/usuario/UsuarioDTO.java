package ar.com.colegiotrabsociales.administracion.model.usuario;

import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String uuid;
    private String usuario;
    private List<String> roles;
}
