package com.adam.projectwebflux.service.impl;

import com.adam.projectwebflux.model.User;
import com.adam.projectwebflux.repository.IGenericRepo;
import com.adam.projectwebflux.repository.IRoleRepo;
import com.adam.projectwebflux.repository.IUserRepo;
import com.adam.projectwebflux.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {


    @Autowired
    private IUserRepo repo;

    @Autowired
    private IRoleRepo rolRepo;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepo<User, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public Mono<com.adam.projectwebflux.exception.security.User> searchByUser(String username) {
        Mono<User> monoUser = repo.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u -> {
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol -> {
                        return rolRepo.findById(rol.getId())
                                .map(r -> {
                                    roles.add(r.getName());
                                    return r;
                                });
                    }).collectList().flatMap(list -> {
                        u.setRoles(list);
                        return Mono.just(u);
                    });
        }).flatMap(u -> Mono.just(new com.adam.projectwebflux.exception.security.User(u.getUsername(), u.getPassword(), u.getStatus(), roles)));
    }

}
