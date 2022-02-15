package de.bildwerkmedien.fluidqr.server.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerkmedien.fluidqr.server.IntegrationTest;
import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GoogleUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoogleUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/google-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGoogleUserMockMvc;

    private GoogleUser googleUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleUser createEntity(EntityManager em) {
        GoogleUser googleUser = new GoogleUser().name(DEFAULT_NAME).refreshToken(DEFAULT_REFRESH_TOKEN);
        return googleUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleUser createUpdatedEntity(EntityManager em) {
        GoogleUser googleUser = new GoogleUser().name(UPDATED_NAME).refreshToken(UPDATED_REFRESH_TOKEN);
        return googleUser;
    }

    @BeforeEach
    public void initTest() {
        googleUser = createEntity(em);
    }

    @Test
    @Transactional
    void getAllGoogleUsers() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(googleUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)));
    }

    @Test
    @Transactional
    void getGoogleUser() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get the googleUser
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL_ID, googleUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(googleUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.refreshToken").value(DEFAULT_REFRESH_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingGoogleUser() throws Exception {
        // Get the googleUser
        restGoogleUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
