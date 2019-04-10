package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.DocumentResource;
import com.ttn.linksharing.repository.DocumentResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentResourceService {

    @Autowired
    DocumentResourceRepository documentResourceRepository;

    public void shareDocument(DocumentResource documentResource){
        documentResourceRepository.save(documentResource);
    }

    public String storeDocument(MultipartFile document) {
        try (InputStream inputStream = document.getInputStream()) {
            if (document.getContentType().split("/")[0].equals("document")) {
                String filename = StringUtils.cleanPath(document.getOriginalFilename());
                Files.copy(inputStream, Paths.get("/home/ttn/project/uploads/documents").resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                return filename;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
