package com.adam.projectwebflux.service;
import com.adam.projectwebflux.model.User;
import reactor.core.publisher.Mono;
public interface IUserService extends ICRUD<User, String>{

    Mono<User> saveHash(User user);
    Mono<com.adam.projectwebflux.exception.security.User> searchByUser(String username);
}
