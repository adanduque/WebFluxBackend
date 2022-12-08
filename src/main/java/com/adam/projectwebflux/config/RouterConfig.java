package com.adam.projectwebflux.config;


import com.adam.projectwebflux.handler.CourseHandler;
import com.adam.projectwebflux.handler.InscriptionHandler;
import com.adam.projectwebflux.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Configuration
public class RouterConfig {


    //Functional Endpoints
    @Bean
    public RouterFunction<ServerResponse> routesInscription(InscriptionHandler handler){
        return route(GET("/v2/inscriptions"), handler::findAll)
                .andRoute(GET("/v2/inscriptions/{id}"), handler::findById)
                .andRoute(POST("/v2/inscriptions"), handler::create)
                .andRoute(PUT("/v2/inscriptions/{id}"), handler::update)
                .andRoute(DELETE("/v2/inscriptions/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesCourse(CourseHandler handler){
        return route(GET("/v2/courses"), handler::findAll)
                .andRoute(GET("/v2/courses/{id}"), handler::findById)
                .andRoute(POST("/v2/courses"), handler::create)
                .andRoute(PUT("/v2/courses/{id}"), handler::update)
                .andRoute(DELETE("/v2/courses/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesInvoices(StudentHandler handler){
        return route(GET("/v2/students"), handler::findAll)
                .andRoute(GET("/v2/findAllStudentOrderByAgeDesc"),handler::findAllStudentOrderByAgeDesc)
                .andRoute(GET("/v2/findAllStudentOrderByAgeAsc"),handler::findAllStudentOrderByAgeAsc)
                .andRoute(GET("/v2/students/{id}"), handler::findById)
                .andRoute(POST("/v2/students"), handler::create)
                .andRoute(PUT("/v2/students/{id}"), handler::update)
                .andRoute(DELETE("/v2/students/{id}"), handler::delete);


    }

}
