package com.gil.easyjwt.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("easy-jwt")
public class JwtProperties {
    private boolean enabled = true;
    private String secret;
    private Integer accessExp;
    private Integer refreshExp;
    private boolean authDetailsService;
}
