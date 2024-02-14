package com.gil.easyjwt.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor
public abstract class JwtUser {

    private String username;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(String username) {
        this.username = username;
        this.authorities = null;
    }

    public JwtUser(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }
}
