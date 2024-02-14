package com.gil.easyjwt.configuration;

import com.gil.easyjwt.jwt.JwtProperties;
import com.gil.easyjwt.jwt.JwtTokenProvider;
import com.gil.easyjwt.auth.JwtUserDetailsService;
import com.gil.easyjwt.user.FakeQueryJwtUserService;
import com.gil.easyjwt.user.QueryJwtUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@AutoConfiguration
@ConditionalOnProperty(name = "easy-jwt.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@EnableConfigurationProperties(JwtProperties.class)
public class EasyJwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(QueryJwtUserService.class)
    public QueryJwtUserService queryJwtUserService() {
        return new FakeQueryJwtUserService();
    }

    @Bean
    public JwtUserDetailsService authDetailsService(QueryJwtUserService queryJwtUserService) {
        return new JwtUserDetailsService(queryJwtUserService);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(JwtUserDetailsService jwtUserDetailsService, JwtProperties jwtProperties) {
        return new JwtTokenProvider(jwtUserDetailsService, jwtProperties);
    }
}
