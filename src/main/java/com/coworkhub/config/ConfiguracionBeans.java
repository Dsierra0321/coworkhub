package com.coworkhub.config;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConfiguracionBeans {

    // Reloj inyectable: si coworkhub.reloj.fijo tiene valor, "ahora" queda congelado
    // en ese instante (útil para probar reglas que dependen de la hora).
    @Bean
    public Clock reloj(@Value("${coworkhub.reloj.fijo:}") String instanteFijo) {
        ZoneId zona = ZoneId.systemDefault();
        if (instanteFijo.isBlank()) {
            return Clock.system(zona);
        }
        return Clock.fixed(LocalDateTime.parse(instanteFijo).atZone(zona).toInstant(), zona);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
