package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.FluidQrServerApp;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.domain.User;
import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.repository.RedirectionRepository;
import de.bildwerkmedien.fluidqr.server.repository.UserRepository;
import de.bildwerkmedien.fluidqr.server.service.RedirectionService;
import de.bildwerkmedien.fluidqr.server.service.dto.RedirectionCriteria;
import de.bildwerkmedien.fluidqr.server.service.RedirectionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static de.bildwerkmedien.fluidqr.server.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RedirectionResource} REST controller.
 */
@SpringBootTest(classes = FluidQrServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RedirectionResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final ZonedDateTime DEFAULT_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private RedirectionRepository redirectionRepository;

    @Autowired
    private RedirectionService redirectionService;

    @Autowired
    private RedirectionQueryService redirectionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRedirectionMockMvc;

    private Redirection redirection;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Redirection createEntity(EntityManager em) {
        Redirection redirection = new Redirection()
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE)
            .url(DEFAULT_URL)
            .enabled(DEFAULT_ENABLED)
            .creation(DEFAULT_CREATION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .user(UserResourceIT.create());

        return redirection;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Redirection createUpdatedEntity(EntityManager em) {
        Redirection redirection = new Redirection()
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .url(UPDATED_URL)
            .enabled(UPDATED_ENABLED)
            .creation(UPDATED_CREATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return redirection;
    }

    @BeforeEach
    public void initTest() {
        redirection = createEntity(em);
    }

    @Test
    @Transactional
    public void createRedirection() throws Exception {
        int databaseSizeBeforeCreate = redirectionRepository.findAll().size();
        // Create the Redirection
        restRedirectionMockMvc.perform(post("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(redirection)))
            .andExpect(status().isCreated());

        // Validate the Redirection in the database
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeCreate + 1);
        Redirection testRedirection = redirectionList.get(redirectionList.size() - 1);
        assertThat(testRedirection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRedirection.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRedirection.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testRedirection.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createRedirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = redirectionRepository.findAll().size();

        // Create the Redirection with an existing ID
        redirection.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRedirectionMockMvc.perform(post("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(redirection)))
            .andExpect(status().isBadRequest());

        // Validate the Redirection in the database
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = redirectionRepository.findAll().size();
        // set the field null
        redirection.setUrl(null);

        // Create the Redirection, which fails.


        restRedirectionMockMvc.perform(post("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(redirection)))
            .andExpect(status().isBadRequest());

        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = redirectionRepository.findAll().size();
        // set the field null
        redirection.setEnabled(null);

        // Create the Redirection, which fails.


        restRedirectionMockMvc.perform(post("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(redirection)))
            .andExpect(status().isBadRequest());

        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRedirections() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList
        restRedirectionMockMvc.perform(get("/api/redirections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redirection.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRedirection() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get the redirection
        restRedirectionMockMvc.perform(get("/api/redirections/{id}", redirection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(redirection.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getRedirectionsByIdFiltering() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        Long id = redirection.getId();

        defaultRedirectionShouldBeFound("id.equals=" + id);
        defaultRedirectionShouldNotBeFound("id.notEquals=" + id);

        defaultRedirectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRedirectionShouldNotBeFound("id.greaterThan=" + id);

        defaultRedirectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRedirectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getRedirectionByIdForOwnUserOnly() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        redirection.setUser(user);
        redirectionRepository.saveAndFlush(redirection);

        // Get the redirection
        restRedirectionMockMvc.perform(get("/api/qr-codes/{id}", redirection.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getRedirectionForOwnUserOnly() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        redirection.setUser(user);
        redirectionRepository.saveAndFlush(redirection);

        // Get the redirection
        restRedirectionMockMvc.perform(get("/api/qr-codes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(empty())));
    }


    @Test
    @Transactional
    public void getAllRedirectionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description equals to DEFAULT_DESCRIPTION
        defaultRedirectionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the redirectionList where description equals to UPDATED_DESCRIPTION
        defaultRedirectionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description not equals to DEFAULT_DESCRIPTION
        defaultRedirectionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the redirectionList where description not equals to UPDATED_DESCRIPTION
        defaultRedirectionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRedirectionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the redirectionList where description equals to UPDATED_DESCRIPTION
        defaultRedirectionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description is not null
        defaultRedirectionShouldBeFound("description.specified=true");

        // Get all the redirectionList where description is null
        defaultRedirectionShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllRedirectionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description contains DEFAULT_DESCRIPTION
        defaultRedirectionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the redirectionList where description contains UPDATED_DESCRIPTION
        defaultRedirectionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where description does not contain DEFAULT_DESCRIPTION
        defaultRedirectionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the redirectionList where description does not contain UPDATED_DESCRIPTION
        defaultRedirectionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code equals to DEFAULT_CODE
        defaultRedirectionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the redirectionList where code equals to UPDATED_CODE
        defaultRedirectionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code not equals to DEFAULT_CODE
        defaultRedirectionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the redirectionList where code not equals to UPDATED_CODE
        defaultRedirectionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultRedirectionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the redirectionList where code equals to UPDATED_CODE
        defaultRedirectionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code is not null
        defaultRedirectionShouldBeFound("code.specified=true");

        // Get all the redirectionList where code is null
        defaultRedirectionShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllRedirectionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code contains DEFAULT_CODE
        defaultRedirectionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the redirectionList where code contains UPDATED_CODE
        defaultRedirectionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where code does not contain DEFAULT_CODE
        defaultRedirectionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the redirectionList where code does not contain UPDATED_CODE
        defaultRedirectionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url equals to DEFAULT_URL
        defaultRedirectionShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the redirectionList where url equals to UPDATED_URL
        defaultRedirectionShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url not equals to DEFAULT_URL
        defaultRedirectionShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the redirectionList where url not equals to UPDATED_URL
        defaultRedirectionShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url in DEFAULT_URL or UPDATED_URL
        defaultRedirectionShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the redirectionList where url equals to UPDATED_URL
        defaultRedirectionShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url is not null
        defaultRedirectionShouldBeFound("url.specified=true");

        // Get all the redirectionList where url is null
        defaultRedirectionShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllRedirectionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url contains DEFAULT_URL
        defaultRedirectionShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the redirectionList where url contains UPDATED_URL
        defaultRedirectionShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where url does not contain DEFAULT_URL
        defaultRedirectionShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the redirectionList where url does not contain UPDATED_URL
        defaultRedirectionShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where enabled equals to DEFAULT_ENABLED
        defaultRedirectionShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the redirectionList where enabled equals to UPDATED_ENABLED
        defaultRedirectionShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where enabled not equals to DEFAULT_ENABLED
        defaultRedirectionShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the redirectionList where enabled not equals to UPDATED_ENABLED
        defaultRedirectionShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultRedirectionShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the redirectionList where enabled equals to UPDATED_ENABLED
        defaultRedirectionShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where enabled is not null
        defaultRedirectionShouldBeFound("enabled.specified=true");

        // Get all the redirectionList where enabled is null
        defaultRedirectionShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation equals to DEFAULT_CREATION
        defaultRedirectionShouldBeFound("creation.equals=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation equals to UPDATED_CREATION
        defaultRedirectionShouldNotBeFound("creation.equals=" + UPDATED_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation not equals to DEFAULT_CREATION
        defaultRedirectionShouldNotBeFound("creation.notEquals=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation not equals to UPDATED_CREATION
        defaultRedirectionShouldBeFound("creation.notEquals=" + UPDATED_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation in DEFAULT_CREATION or UPDATED_CREATION
        defaultRedirectionShouldBeFound("creation.in=" + DEFAULT_CREATION + "," + UPDATED_CREATION);

        // Get all the redirectionList where creation equals to UPDATED_CREATION
        defaultRedirectionShouldNotBeFound("creation.in=" + UPDATED_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation is not null
        defaultRedirectionShouldBeFound("creation.specified=true");

        // Get all the redirectionList where creation is null
        defaultRedirectionShouldNotBeFound("creation.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation is greater than or equal to DEFAULT_CREATION
        defaultRedirectionShouldBeFound("creation.greaterThanOrEqual=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation is greater than or equal to UPDATED_CREATION
        defaultRedirectionShouldNotBeFound("creation.greaterThanOrEqual=" + UPDATED_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation is less than or equal to DEFAULT_CREATION
        defaultRedirectionShouldBeFound("creation.lessThanOrEqual=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation is less than or equal to SMALLER_CREATION
        defaultRedirectionShouldNotBeFound("creation.lessThanOrEqual=" + SMALLER_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation is less than DEFAULT_CREATION
        defaultRedirectionShouldNotBeFound("creation.lessThan=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation is less than UPDATED_CREATION
        defaultRedirectionShouldBeFound("creation.lessThan=" + UPDATED_CREATION);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where creation is greater than DEFAULT_CREATION
        defaultRedirectionShouldNotBeFound("creation.greaterThan=" + DEFAULT_CREATION);

        // Get all the redirectionList where creation is greater than SMALLER_CREATION
        defaultRedirectionShouldBeFound("creation.greaterThan=" + SMALLER_CREATION);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate equals to DEFAULT_START_DATE
        defaultRedirectionShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate equals to UPDATED_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate not equals to DEFAULT_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate not equals to UPDATED_START_DATE
        defaultRedirectionShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultRedirectionShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the redirectionList where startDate equals to UPDATED_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate is not null
        defaultRedirectionShouldBeFound("startDate.specified=true");

        // Get all the redirectionList where startDate is null
        defaultRedirectionShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultRedirectionShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate is greater than or equal to UPDATED_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate is less than or equal to DEFAULT_START_DATE
        defaultRedirectionShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate is less than or equal to SMALLER_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate is less than DEFAULT_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate is less than UPDATED_START_DATE
        defaultRedirectionShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where startDate is greater than DEFAULT_START_DATE
        defaultRedirectionShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the redirectionList where startDate is greater than SMALLER_START_DATE
        defaultRedirectionShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate equals to DEFAULT_END_DATE
        defaultRedirectionShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate equals to UPDATED_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate not equals to DEFAULT_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate not equals to UPDATED_END_DATE
        defaultRedirectionShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultRedirectionShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the redirectionList where endDate equals to UPDATED_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate is not null
        defaultRedirectionShouldBeFound("endDate.specified=true");

        // Get all the redirectionList where endDate is null
        defaultRedirectionShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultRedirectionShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate is greater than or equal to UPDATED_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate is less than or equal to DEFAULT_END_DATE
        defaultRedirectionShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate is less than or equal to SMALLER_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate is less than DEFAULT_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate is less than UPDATED_END_DATE
        defaultRedirectionShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRedirectionsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);

        // Get all the redirectionList where endDate is greater than DEFAULT_END_DATE
        defaultRedirectionShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the redirectionList where endDate is greater than SMALLER_END_DATE
        defaultRedirectionShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);
        User user = UserResourceIT.create();
        redirection.setUser(user);
        redirectionRepository.saveAndFlush(redirection);
        Long userId = user.getId();

        // Get all the redirectionList where user equals to userId
        defaultRedirectionShouldBeFound("userId.equals=" + userId);
    }


    @Test
    @Transactional
    public void getAllRedirectionsByQrCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        redirectionRepository.saveAndFlush(redirection);
        QrCode qrCode = QrCodeResourceIT.createEntity(em);
        em.persist(qrCode);
        em.flush();
        redirection.setQrCode(qrCode);
        redirectionRepository.saveAndFlush(redirection);
        Long qrCodeId = qrCode.getId();

        // Get all the redirectionList where qrCode equals to qrCodeId
        defaultRedirectionShouldBeFound("qrCodeId.equals=" + qrCodeId);

        // Get all the redirectionList where qrCode equals to qrCodeId + 1
        defaultRedirectionShouldNotBeFound("qrCodeId.equals=" + (qrCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRedirectionShouldBeFound(String filter) throws Exception {
        restRedirectionMockMvc.perform(get("/api/redirections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redirection.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restRedirectionMockMvc.perform(get("/api/redirections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRedirectionShouldNotBeFound(String filter) throws Exception {
        restRedirectionMockMvc.perform(get("/api/redirections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRedirectionMockMvc.perform(get("/api/redirections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRedirection() throws Exception {
        // Get the redirection
        restRedirectionMockMvc.perform(get("/api/redirections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRedirection() throws Exception {
        // Initialize the database
        redirectionService.save(redirection);

        int databaseSizeBeforeUpdate = redirectionRepository.findAll().size();

        // Update the redirection
        Redirection updatedRedirection = redirectionRepository.findById(redirection.getId()).get();
        // Disconnect from session so that the updates on updatedRedirection are not directly saved in db
        em.detach(updatedRedirection);
        updatedRedirection
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .url(UPDATED_URL)
            .enabled(UPDATED_ENABLED)
            .creation(UPDATED_CREATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restRedirectionMockMvc.perform(put("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRedirection)))
            .andExpect(status().isOk());

        // Validate the Redirection in the database
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeUpdate);
        Redirection testRedirection = redirectionList.get(redirectionList.size() - 1);
        assertThat(testRedirection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRedirection.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRedirection.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testRedirection.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingRedirection() throws Exception {
        int databaseSizeBeforeUpdate = redirectionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedirectionMockMvc.perform(put("/api/redirections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(redirection)))
            .andExpect(status().isBadRequest());

        // Validate the Redirection in the database
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateRedirectionWithAnotherUserIsFailing() throws Exception {

        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        redirection.setUser(user);
        redirectionRepository.save(redirection);

        // Update the redirection
        Redirection updatedRedirection = redirectionRepository.findById(redirection.getId()).get();
        // Disconnect from session so that the updates on updatedRedirection are not directly saved in db
        em.detach(redirection);

        updatedRedirection
            .code(UPDATED_CODE);

        restRedirectionMockMvc.perform(put("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRedirection)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void deleteRedirection() throws Exception {
        // Initialize the database
        redirectionService.save(redirection);

        int databaseSizeBeforeDelete = redirectionRepository.findAll().size();

        // Delete the redirection
        restRedirectionMockMvc.perform(delete("/api/redirections/{id}", redirection.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void doNotDeleteRedirectionFromOtherUser() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        redirection.setUser(user);
        redirectionRepository.save(redirection);

        int databaseSizeBeforeDelete = redirectionRepository.findAll().size();

        // Delete the redirection
        restRedirectionMockMvc.perform(delete("/api/qr-codes/{id}", redirection.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Redirection> redirectionList = redirectionRepository.findAll();
        assertThat(redirectionList).hasSize(databaseSizeBeforeDelete);
    }
}
