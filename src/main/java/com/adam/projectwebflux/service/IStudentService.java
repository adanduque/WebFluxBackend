package com.adam.projectwebflux.service;

import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.StudentModel;
import reactor.core.publisher.Flux;

public interface IStudentService extends ICRUD<StudentModel,String>{

    Flux<StudentModel>  findAllStudentOrderByAgeDesc();

    Flux<StudentModel>  findAllStudentOrderByAgeAsc();
}
