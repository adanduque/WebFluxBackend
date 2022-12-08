package com.adam.projectwebflux.handler;

import com.adam.projectwebflux.exception.security.validator.RequestValidator;
import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.service.IStudentService;
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
public class StudentHandler {
    @Autowired
    private IStudentService service;



    @Autowired
    private RequestValidator requestValidator_a;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), StudentModel.class);
    }

    public Mono<ServerResponse> findAllStudentOrderByAgeDesc(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAllStudentOrderByAgeDesc(), StudentModel.class);
    }

    public Mono<ServerResponse> findAllStudentOrderByAgeAsc(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAllStudentOrderByAgeAsc(), StudentModel.class);
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
        Mono<StudentModel> monoStudent = req.bodyToMono(StudentModel.class);

        return monoStudent
                .flatMap(requestValidator_a::validate)
                .flatMap(service::save)
                .flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(String.valueOf(p.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                );
    }

    public Mono<ServerResponse> update(ServerRequest req) {

        Mono<StudentModel> monoStudent = req.bodyToMono(StudentModel.class);
        Mono<StudentModel> monoBD = service.findById(req.pathVariable("id"));

        return monoBD
                .zipWith(monoStudent, (bd, p) -> {
                    bd.setId(bd.getId());
                    bd.setAge(p.getAge());
                    bd.setDni(p.getDni());
                    bd.setFirstName(p.getFirstName());
                    bd.setLastName(p.getLastName());
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
