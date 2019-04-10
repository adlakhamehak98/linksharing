package com.ttn.linksharing.service;

import com.ttn.linksharing.entity.LinkResource;
import com.ttn.linksharing.repository.LinkResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkResourceService {
    @Autowired
    LinkResourceRepository linkResourceRepository;

    public void shareLink(LinkResource linkResource) {
        linkResourceRepository.save(linkResource);
    }
}
