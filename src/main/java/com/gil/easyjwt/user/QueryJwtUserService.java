package com.gil.easyjwt.user;

import java.util.Optional;

public interface QueryJwtUserService {
    Optional<JwtUser> execute(String username);
}
