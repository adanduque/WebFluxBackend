package com.adam.projectwebflux.controller;

import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.InscriptionModel;
import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.pagination.PageSupport;
import com.adam.projectwebflux.service.ICourseService;
import com.adam.projectwebflux.service.IInscriptionService;
import com.adam.projectwebflux.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("inscriptions")
public class InscriptionController {

    Logger logger = LoggerFactory.getLogger(InscriptionController.class);

    @Autowired
    private IInscriptionService service;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ICourseService courseService;

    @GetMapping
    public Mono<ResponseEntity<Flux<InscriptionModel>>> findAll() {

        Flux<InscriptionModel> fx = service.findAll().flatMap(
               inscription -> {
                   Mono<StudentModel> student = studentService.findById(inscription.getStudent().getId());
                   Mono<List<CourseModel>> courses = courseService.findAllById(inscription.getIds()).collectList();

                   return  student.zipWith(courses,(st,c)->{
                       logger.info(c.toString());
                       inscription.setStudent(st);
                       inscription.setCourses(c);
                       return inscription;
                   });
               }
        );

        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<InscriptionModel>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .flatMap(inscription -> {
                            Mono<StudentModel> student = studentService.findById(inscription.getStudent().getId());
                            Mono<List<CourseModel>> courses = courseService.findAllById(inscription.getIds()).collectList();
                          return  student.zipWith(courses,(st,c)->{
                                logger.info(c.toString());
                                inscription.setStudent(st);
                                inscription.setCourses(c);
                                return  ResponseEntity.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(inscription);
                            });
                        }
                );


    }

    @PostMapping
    public Mono<ResponseEntity<InscriptionModel>> save(@Valid @RequestBody InscriptionModel inscription, final ServerHttpRequest req) {
        return service.save(inscription) //Mono<Client>
                .map(e -> ResponseEntity
                        .created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<InscriptionModel>> update(@Valid @PathVariable("id") String id, @RequestBody InscriptionModel inscription) {
        inscription.setId(id);

        Mono<InscriptionModel> monoBody = Mono.just(inscription);
        Mono<InscriptionModel> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, c) -> {
                    db.setId(id);
                    db.setCourses(c.getCourses());
                    db.setDate(c.getDate());
                    db.setState(c.getState());
                    db.setStudent(c.getStudent());
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
    public Mono<ResponseEntity<PageSupport<InscriptionModel>>> getPage(
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
