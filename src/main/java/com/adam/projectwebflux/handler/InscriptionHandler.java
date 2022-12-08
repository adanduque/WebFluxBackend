package com.adam.projectwebflux.handler;

import com.adam.projectwebflux.exception.security.validator.RequestValidator;
import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.InscriptionModel;
import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.service.ICourseService;
import com.adam.projectwebflux.service.IInscriptionService;
import com.adam.projectwebflux.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class InscriptionHandler {
    @Autowired
    private IInscriptionService service;


    @Autowired
    private RequestValidator requestValidator_a;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ICourseService courseService;
    public Mono<ServerResponse> findAll(ServerRequest req){

        Flux<InscriptionModel> fx = service.findAll().flatMap(
                inscription -> {
                    Mono<StudentModel> student = studentService.findById(inscription.getStudent().getId());
                    Mono<List<CourseModel>> courses = courseService.findAllById(inscription.getIds()).collectList();

                    return  student.zipWith(courses,(st,c)->{
                        inscription.setStudent(st);
                        inscription.setCourses(c);
                        return inscription;
                    });
                }
        );

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx, InscriptionModel.class).switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<InscriptionModel> fx = service.findById(id).flatMap(
                inscription -> {
                    Mono<StudentModel> student = studentService.findById(inscription.getStudent().getId());
                    Mono<List<CourseModel>> courses = courseService.findAllById(inscription.getIds()).collectList();
                    return  student.zipWith(courses,(st,c)->{
                        inscription.setStudent(st);
                        inscription.setCourses(c);
                        return inscription;
                    });
                }
        );

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx, InscriptionModel.class)
                .switchIfEmpty(ServerResponse.notFound().build());


    }

    public Mono<ServerResponse> create(ServerRequest req) {
        Mono<InscriptionModel> monoInscription = req.bodyToMono(InscriptionModel.class);

        return monoInscription
                .flatMap(requestValidator_a::validate)
                .flatMap(service::save)
                .flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(String.valueOf(p.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                );
    }

    public Mono<ServerResponse> update(ServerRequest req) {

        Mono<InscriptionModel> monoInscription = req.bodyToMono(InscriptionModel.class);
        Mono<InscriptionModel> monoBD = service.findById(req.pathVariable("id"));

        return monoBD
                .zipWith(monoInscription, (bd, p) -> {
                    bd.setId(bd.getId());
                    bd.setCourses(p.getCourses());
                    bd.setStudent(p.getStudent());
                    bd.setState(p.getState());
                    bd.setDate(p.getDate());
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
