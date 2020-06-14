package com.lingogame.configuration.security;

public interface ISecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
