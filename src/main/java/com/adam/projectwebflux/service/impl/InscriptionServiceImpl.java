package com.adam.projectwebflux.service.impl;

import com.adam.projectwebflux.model.InscriptionModel;
import com.adam.projectwebflux.repository.IGenericRepo;
import com.adam.projectwebflux.repository.IInscriptionRepo;
import com.adam.projectwebflux.service.IInscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionServiceImpl extends CRUDImpl<InscriptionModel,String> implements IInscriptionService {

    @Autowired
    private IInscriptionRepo repo;

    @Override
    protected IGenericRepo<InscriptionModel, String> getRepo() {
        return repo;
    }
}
