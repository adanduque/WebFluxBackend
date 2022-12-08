package com.adam.projectwebflux.service.impl;

import com.adam.projectwebflux.model.CourseModel;
import com.adam.projectwebflux.model.StudentModel;
import com.adam.projectwebflux.repository.ICourseRepo;
import com.adam.projectwebflux.repository.IGenericRepo;
import com.adam.projectwebflux.repository.IStudentRepo;
import com.adam.projectwebflux.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StudentServiceImpl extends CRUDImpl<StudentModel,String> implements IStudentService {
    @Autowired
    private IStudentRepo repo;

    @Override
    protected IGenericRepo<StudentModel, String> getRepo() {
        return repo;
    }

    @Override
    public Flux<StudentModel> findAllStudentOrderByAgeDesc(){
      return  this.repo.findAll(Sort.by(Sort.Direction.DESC, "age"));
    }

    @Override
    public Flux<StudentModel>  findAllStudentOrderByAgeAsc(){
        return  this.repo.findAll(Sort.by(Sort.Direction.ASC, "age"));
    }
}
