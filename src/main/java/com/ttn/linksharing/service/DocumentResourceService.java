package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.DocumentResource;
import com.ttn.linksharing.repository.DocumentResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentResourceService {

    @Autowired
    DocumentResourceRepository documentResourceRepository;

    public void shareDocument(DocumentResource documentResource){
        documentResourceRepository.save(documentResource);
    }
}
