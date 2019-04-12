package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.DocumentResource;
import com.ttn.linksharing.repository.DocumentResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class DocumentResourceService {

    @Autowired
    DocumentResourceRepository documentResourceRepository;

    public void shareDocument(DocumentResource documentResource) {
        documentResourceRepository.save(documentResource);
    }

    public String storeDocument(MultipartFile document) {
        try {
            String filename = StringUtils.cleanPath(document.getOriginalFilename());
            document.transferTo(new File("/home/ttn/project/uploads/documents/" + filename));
            return "/home/ttn/project/uploads/documents/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
