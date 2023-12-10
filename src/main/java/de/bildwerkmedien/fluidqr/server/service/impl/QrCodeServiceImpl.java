package de.bildwerkmedien.fluidqr.server.service.impl;

import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.service.QrCodeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.bildwerkmedien.fluidqr.server.domain.QrCode}.
 */
@Service
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final Logger log = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    private final QrCodeRepository qrCodeRepository;

    public QrCodeServiceImpl(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    public QrCode save(QrCode qrCode) {
        log.debug("Request to save QrCode : {}", qrCode);
        return qrCodeRepository.save(qrCode);
    }

    @Override
    public QrCode update(QrCode qrCode) {
        log.debug("Request to update QrCode : {}", qrCode);
        return qrCodeRepository.save(qrCode);
    }

    @Override
    public Optional<QrCode> partialUpdate(QrCode qrCode) {
        log.debug("Request to partially update QrCode : {}", qrCode);

        return qrCodeRepository
            .findById(qrCode.getId())
            .map(existingQrCode -> {
                if (qrCode.getCode() != null) {
                    existingQrCode.setCode(qrCode.getCode());
                }

                return existingQrCode;
            })
            .map(qrCodeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrCode> findAll(Pageable pageable) {
        log.debug("Request to get all QrCodes");
        return qrCodeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QrCode> findOne(Long id) {
        log.debug("Request to get QrCode : {}", id);
        return qrCodeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrCode : {}", id);
        qrCodeRepository.deleteById(id);
    }
}
