package com.gil.easyjwt.auth;

import com.gil.easyjwt.user.QueryJwtUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final QueryJwtUserService queryJwtUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryJwtUserService.execute(username)
                .map(JwtUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("JwtUser Not Found"));
    }
}
