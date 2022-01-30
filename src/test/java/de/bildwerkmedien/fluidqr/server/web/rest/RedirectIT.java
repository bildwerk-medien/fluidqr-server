package de.bildwerkmedien.fluidqr.server.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.bildwerkmedien.fluidqr.server.FluidQrServerApp;
import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.repository.UserRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RedirectionResource} REST controller.
 */
@SpringBootTest(classes = FluidQrServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RedirectIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Autowired
    private MockMvc restQrCodeMockMvc;

    @Autowired
    private EntityManager em;

    private QrCode qrCode;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCode createEntity(EntityManager em, UserRepository userRepository) {
        QrCode qrCode = new QrCode().code(DEFAULT_CODE).user(userRepository.getById(2L));
        return qrCode;
    }

    @BeforeEach
    public void initTest() {
        qrCode = createEntity(em, userRepository);
    }

    @Test
    @Transactional
    public void redirectWithValidCodeExpectUrlOfCurrentRedirectionForQrCode() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);
        Redirection redirection = RedirectionResourceIT.createEntity(em, userRepository);
        redirection.setEnabled(true);
        em.persist(redirection);
        em.flush();
        qrCode.addRedirection(redirection);
        qrCodeRepository.saveAndFlush(qrCode);

        //Get redirect
        restQrCodeMockMvc
            .perform(get("/go/{code}", DEFAULT_CODE))
            .andExpect(status().isTemporaryRedirect())
            .andExpect(header().string("Location", DEFAULT_URL))
            .andExpect(header().string("Cache-Control", "no-store"));
    }

    @Test
    @Transactional
    public void redirectWithInvalidCodeExpectNotFound() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);
        Redirection redirection = RedirectionResourceIT.createEntity(em, userRepository);
        redirection.setEnabled(true);
        em.persist(redirection);
        em.flush();
        qrCode.addRedirection(redirection);
        qrCodeRepository.saveAndFlush(qrCode);

        //Get redirect
        restQrCodeMockMvc.perform(get("/go/{code}", UPDATED_CODE)).andExpect(status().isNotFound());
    }
}
