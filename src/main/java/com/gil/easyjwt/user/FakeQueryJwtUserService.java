package com.gil.easyjwt.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FakeQueryJwtUserService implements QueryJwtUserService {
    @Override
    public Optional<JwtUser> execute(String username) {
        return Optional.empty();
    }
}
