package com.adam.projectwebflux.service;

import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.StudentModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ICourseService extends ICRUD<CourseModel,String>{

    Flux<CourseModel> findAllById(List<String> idCourses);


}
