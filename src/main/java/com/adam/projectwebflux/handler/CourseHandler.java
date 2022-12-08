package com.adam.projectwebflux.handler;

import com.adam.projectwebflux.exception.security.validator.RequestValidator;
import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.service.ICourseService;
import com.adam.projectwebflux.validator.RequestValidator_A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class CourseHandler {
    @Autowired
    private ICourseService service;



    @Autowired
    private RequestValidator_A requestValidator_a;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), CourseModel.class);
    }


    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");
        return service.findById(id)
                .flatMap(p -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        Mono<CourseModel> monoCourse = req.bodyToMono(CourseModel.class);

        return monoCourse
                .flatMap(requestValidator_a::validate)
                .flatMap(service::save)
                .flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(String.valueOf(p.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                );
    }

    public Mono<ServerResponse> update(ServerRequest req) {

        Mono<CourseModel> monoCourse = req.bodyToMono(CourseModel.class);
        Mono<CourseModel> monoBD = service.findById(req.pathVariable("id"));

        return monoBD
                .zipWith(monoCourse, (bd, p) -> {
                    bd.setId(bd.getId());
                    bd.setAcronym(p.getAcronym());
                    bd.setName(p.getName());
                    bd.setState(p.getState());
                    return bd;
                })
                .flatMap(requestValidator_a::validate)
                .flatMap(service::update)
                .flatMap(p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(p -> service.delete(p.getId())
                        .then(ServerResponse.noContent().build())
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
