package com.lingogame.configuration.security;

import com.lingogame.model.User;

public interface IUserService {
    User save(User user);

    User findByUsername(String username);
}
