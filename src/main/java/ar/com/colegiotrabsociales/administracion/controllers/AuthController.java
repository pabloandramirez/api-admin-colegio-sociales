package ar.com.colegiotrabsociales.administracion.controllers;

import ar.com.colegiotrabsociales.administracion.config.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", "false"));
        }

        String token = authHeader.substring(7);
        String isValid = String.valueOf(jwtUtils.isTokenValid(token));
        String username = "";
        String role = jwtUtils.getRoleFromToken(token);
        if (Boolean.parseBoolean(isValid)){
            username = jwtUtils.getUsernameFromToken(token);
        }
        return ResponseEntity.ok(Map.of("valid", isValid , "username", username, "role", role));
    }
}