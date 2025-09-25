package ec.com.carrera.makers.security.config;

import ec.com.carrera.makers.security.exception.OAuth2ExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final OAuth2ExceptionHandler securityExceptionHandler;

    public SecurityConfiguration(OAuth2ExceptionHandler securityExceptionHandler) {
        this.securityExceptionHandler = securityExceptionHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        //.pathMatchers("/**/api/public/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                );

        return http.build();
    }
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            List<GrantedAuthority> combinedAuthorities = new ArrayList<>(authorities);

            List<String> realmRoles = jwt.getClaimAsMap("realm_access") != null ?
                    ((List<?>) jwt.getClaimAsMap("realm_access").get("roles")).stream()
                            .map(String::valueOf)
                            .toList() : List.of();

            realmRoles.forEach(role -> combinedAuthorities.add(
                    new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
            ));

            return combinedAuthorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
