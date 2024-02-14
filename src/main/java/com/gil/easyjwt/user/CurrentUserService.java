package com.gil.easyjwt.user;

import com.gil.easyjwt.auth.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class CurrentUserService<T extends JwtUser> {

    public T getCurrentUser() {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (T) jwtUserDetails.getJwtUser();
    }
}
