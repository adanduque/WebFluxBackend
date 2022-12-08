package com.adam.projectwebflux.service.impl;

import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.repository.ICourseRepo;
import com.adam.projectwebflux.repository.IGenericRepo;
import com.adam.projectwebflux.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class CourseServiceImpl extends CRUDImpl<CourseModel,String> implements ICourseService {

    @Autowired
    private ICourseRepo repo;

    @Override
    protected IGenericRepo<CourseModel, String> getRepo() {
        return repo;
    }


    @Override
    public Flux<CourseModel> findAllById(List<String> idCourses) {
        return repo.findAllById(idCourses);
    }
}
