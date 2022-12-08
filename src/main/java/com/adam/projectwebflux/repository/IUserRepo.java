package com.adam.projectwebflux.repository;
import com.adam.projectwebflux.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User, String>{

    //@Query("{username: ?}")
    //DerivedQueries
    Mono<User> findOneByUsername(String username);
}
