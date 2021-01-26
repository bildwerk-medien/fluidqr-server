package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.service.RedirectionService;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.repository.RedirectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Redirection}.
 */
@Service
@Transactional
public class RedirectionServiceImpl implements RedirectionService {

    private final Logger log = LoggerFactory.getLogger(RedirectionServiceImpl.class);

    private final RedirectionRepository redirectionRepository;

    public RedirectionServiceImpl(RedirectionRepository redirectionRepository) {
        this.redirectionRepository = redirectionRepository;
    }

    @Override
    public Redirection save(Redirection redirection) {
        log.debug("Request to save Redirection : {}", redirection);
        return redirectionRepository.save(redirection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Redirection> findAll(Pageable pageable) {
        log.debug("Request to get all Redirections");
        return redirectionRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Redirection> findOne(Long id) {
        log.debug("Request to get Redirection : {}", id);
        return redirectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Redirection : {}", id);
        redirectionRepository.deleteById(id);
    }
}
