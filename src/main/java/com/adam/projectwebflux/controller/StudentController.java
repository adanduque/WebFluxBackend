package com.adam.projectwebflux.controller;

import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.adam.projectwebflux.pagination.PageSupport;
import org.springframework.data.domain.PageRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<StudentModel>>> findAll() {
        Flux<StudentModel> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
    @GetMapping("findAllStudentOrderByAgeDesc")
    public Mono<ResponseEntity<Flux<StudentModel>>> findAllStudentOrderByAgeDesc() {
        Flux<StudentModel> fx = service.findAllStudentOrderByAgeDesc();
        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
    @GetMapping("findAllStudentOrderByAgeAsc")
    public Mono<ResponseEntity<Flux<StudentModel>>> findAllStudentOrderByAgeAsc() {
        Flux<StudentModel> fx = service.findAllStudentOrderByAgeAsc();
        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentModel>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<StudentModel>> save(@Valid @RequestBody StudentModel client, final ServerHttpRequest req) {
        return service.save(client) //Mono<Client>
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<StudentModel>> update(@Valid @PathVariable("id") String id, @RequestBody StudentModel client) {
        client.setId(id);

        Mono<StudentModel> monoBody = Mono.just(client);
        Mono<StudentModel> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, c) -> {
                    db.setId(id);
                    db.setFirstName(c.getFirstName());
                    db.setLastName(c.getLastName());
                    db.setDni(c.getDni());
                    db.setAge(c.getAge());
                    return db;
                })
                .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.findById(id)
                .flatMap(e -> service.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //pageable
    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<StudentModel>>> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size
    ){
        return service.getPage(PageRequest.of(page, size))
                .map(pag -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pag)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
