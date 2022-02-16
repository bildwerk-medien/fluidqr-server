package de.bildwerkmedien.fluidqr.server.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerkmedien.fluidqr.server.IntegrationTest;
import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.domain.User;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.service.criteria.GoogleUserCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_CREATION_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
        GoogleUser googleUser = new GoogleUser()
            .name(DEFAULT_NAME)
            .refreshToken(DEFAULT_REFRESH_TOKEN)
            .enabled(DEFAULT_ENABLED)
            .creationTime(DEFAULT_CREATION_TIME);
        return googleUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleUser createUpdatedEntity(EntityManager em) {
        GoogleUser googleUser = new GoogleUser()
            .name(UPDATED_NAME)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .enabled(UPDATED_ENABLED)
            .creationTime(UPDATED_CREATION_TIME);
        return googleUser;
    }

    @BeforeEach
    public void initTest() {
        googleUser = createEntity(em);
    }

    @Test
    @Transactional
    void createGoogleUser() throws Exception {
        int databaseSizeBeforeCreate = googleUserRepository.findAll().size();
        // Create the GoogleUser
        restGoogleUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleUser)))
            .andExpect(status().isCreated());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeCreate + 1);
        GoogleUser testGoogleUser = googleUserList.get(googleUserList.size() - 1);
        assertThat(testGoogleUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoogleUser.getRefreshToken()).isEqualTo(DEFAULT_REFRESH_TOKEN);
        assertThat(testGoogleUser.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testGoogleUser.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
    }

    @Test
    @Transactional
    void createGoogleUserWithExistingId() throws Exception {
        // Create the GoogleUser with an existing ID
        googleUser.setId(1L);

        int databaseSizeBeforeCreate = googleUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoogleUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleUser)))
            .andExpect(status().isBadRequest());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleUserRepository.findAll().size();
        // set the field null
        googleUser.setName(null);

        // Create the GoogleUser, which fails.

        restGoogleUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleUser)))
            .andExpect(status().isBadRequest());

        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME.toString())));
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
            .andExpect(jsonPath("$.refreshToken").value(DEFAULT_REFRESH_TOKEN))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME.toString()));
    }

    @Test
    @Transactional
    void getGoogleUsersByIdFiltering() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        Long id = googleUser.getId();

        defaultGoogleUserShouldBeFound("id.equals=" + id);
        defaultGoogleUserShouldNotBeFound("id.notEquals=" + id);

        defaultGoogleUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGoogleUserShouldNotBeFound("id.greaterThan=" + id);

        defaultGoogleUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGoogleUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name equals to DEFAULT_NAME
        defaultGoogleUserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the googleUserList where name equals to UPDATED_NAME
        defaultGoogleUserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name not equals to DEFAULT_NAME
        defaultGoogleUserShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the googleUserList where name not equals to UPDATED_NAME
        defaultGoogleUserShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGoogleUserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the googleUserList where name equals to UPDATED_NAME
        defaultGoogleUserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name is not null
        defaultGoogleUserShouldBeFound("name.specified=true");

        // Get all the googleUserList where name is null
        defaultGoogleUserShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name contains DEFAULT_NAME
        defaultGoogleUserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the googleUserList where name contains UPDATED_NAME
        defaultGoogleUserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where name does not contain DEFAULT_NAME
        defaultGoogleUserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the googleUserList where name does not contain UPDATED_NAME
        defaultGoogleUserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken equals to DEFAULT_REFRESH_TOKEN
        defaultGoogleUserShouldBeFound("refreshToken.equals=" + DEFAULT_REFRESH_TOKEN);

        // Get all the googleUserList where refreshToken equals to UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldNotBeFound("refreshToken.equals=" + UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken not equals to DEFAULT_REFRESH_TOKEN
        defaultGoogleUserShouldNotBeFound("refreshToken.notEquals=" + DEFAULT_REFRESH_TOKEN);

        // Get all the googleUserList where refreshToken not equals to UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldBeFound("refreshToken.notEquals=" + UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenIsInShouldWork() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken in DEFAULT_REFRESH_TOKEN or UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldBeFound("refreshToken.in=" + DEFAULT_REFRESH_TOKEN + "," + UPDATED_REFRESH_TOKEN);

        // Get all the googleUserList where refreshToken equals to UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldNotBeFound("refreshToken.in=" + UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken is not null
        defaultGoogleUserShouldBeFound("refreshToken.specified=true");

        // Get all the googleUserList where refreshToken is null
        defaultGoogleUserShouldNotBeFound("refreshToken.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenContainsSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken contains DEFAULT_REFRESH_TOKEN
        defaultGoogleUserShouldBeFound("refreshToken.contains=" + DEFAULT_REFRESH_TOKEN);

        // Get all the googleUserList where refreshToken contains UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldNotBeFound("refreshToken.contains=" + UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByRefreshTokenNotContainsSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where refreshToken does not contain DEFAULT_REFRESH_TOKEN
        defaultGoogleUserShouldNotBeFound("refreshToken.doesNotContain=" + DEFAULT_REFRESH_TOKEN);

        // Get all the googleUserList where refreshToken does not contain UPDATED_REFRESH_TOKEN
        defaultGoogleUserShouldBeFound("refreshToken.doesNotContain=" + UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where enabled equals to DEFAULT_ENABLED
        defaultGoogleUserShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the googleUserList where enabled equals to UPDATED_ENABLED
        defaultGoogleUserShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where enabled not equals to DEFAULT_ENABLED
        defaultGoogleUserShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the googleUserList where enabled not equals to UPDATED_ENABLED
        defaultGoogleUserShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultGoogleUserShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the googleUserList where enabled equals to UPDATED_ENABLED
        defaultGoogleUserShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where enabled is not null
        defaultGoogleUserShouldBeFound("enabled.specified=true");

        // Get all the googleUserList where enabled is null
        defaultGoogleUserShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleUsersByCreationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where creationTime equals to DEFAULT_CREATION_TIME
        defaultGoogleUserShouldBeFound("creationTime.equals=" + DEFAULT_CREATION_TIME);

        // Get all the googleUserList where creationTime equals to UPDATED_CREATION_TIME
        defaultGoogleUserShouldNotBeFound("creationTime.equals=" + UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByCreationTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where creationTime not equals to DEFAULT_CREATION_TIME
        defaultGoogleUserShouldNotBeFound("creationTime.notEquals=" + DEFAULT_CREATION_TIME);

        // Get all the googleUserList where creationTime not equals to UPDATED_CREATION_TIME
        defaultGoogleUserShouldBeFound("creationTime.notEquals=" + UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByCreationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where creationTime in DEFAULT_CREATION_TIME or UPDATED_CREATION_TIME
        defaultGoogleUserShouldBeFound("creationTime.in=" + DEFAULT_CREATION_TIME + "," + UPDATED_CREATION_TIME);

        // Get all the googleUserList where creationTime equals to UPDATED_CREATION_TIME
        defaultGoogleUserShouldNotBeFound("creationTime.in=" + UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    void getAllGoogleUsersByCreationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        // Get all the googleUserList where creationTime is not null
        defaultGoogleUserShouldBeFound("creationTime.specified=true");

        // Get all the googleUserList where creationTime is null
        defaultGoogleUserShouldNotBeFound("creationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        googleUser.setUser(user);
        googleUserRepository.saveAndFlush(googleUser);
        Long userId = user.getId();

        // Get all the googleUserList where user equals to userId
        defaultGoogleUserShouldBeFound("userId.equals=" + userId);

        // Get all the googleUserList where user equals to (userId + 1)
        defaultGoogleUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGoogleUserShouldBeFound(String filter) throws Exception {
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(googleUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME.toString())));

        // Check, that the count call also returns 1
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGoogleUserShouldNotBeFound(String filter) throws Exception {
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoogleUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGoogleUser() throws Exception {
        // Get the googleUser
        restGoogleUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGoogleUser() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();

        // Update the googleUser
        GoogleUser updatedGoogleUser = googleUserRepository.findById(googleUser.getId()).get();
        // Disconnect from session so that the updates on updatedGoogleUser are not directly saved in db
        em.detach(updatedGoogleUser);
        updatedGoogleUser
            .name(UPDATED_NAME)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .enabled(UPDATED_ENABLED)
            .creationTime(UPDATED_CREATION_TIME);

        restGoogleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGoogleUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGoogleUser))
            )
            .andExpect(status().isOk());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
        GoogleUser testGoogleUser = googleUserList.get(googleUserList.size() - 1);
        assertThat(testGoogleUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoogleUser.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
        assertThat(testGoogleUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testGoogleUser.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    void putNonExistingGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, googleUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(googleUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(googleUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGoogleUserWithPatch() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();

        // Update the googleUser using partial update
        GoogleUser partialUpdatedGoogleUser = new GoogleUser();
        partialUpdatedGoogleUser.setId(googleUser.getId());

        partialUpdatedGoogleUser.enabled(UPDATED_ENABLED);

        restGoogleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoogleUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoogleUser))
            )
            .andExpect(status().isOk());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
        GoogleUser testGoogleUser = googleUserList.get(googleUserList.size() - 1);
        assertThat(testGoogleUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoogleUser.getRefreshToken()).isEqualTo(DEFAULT_REFRESH_TOKEN);
        assertThat(testGoogleUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testGoogleUser.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
    }

    @Test
    @Transactional
    void fullUpdateGoogleUserWithPatch() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();

        // Update the googleUser using partial update
        GoogleUser partialUpdatedGoogleUser = new GoogleUser();
        partialUpdatedGoogleUser.setId(googleUser.getId());

        partialUpdatedGoogleUser
            .name(UPDATED_NAME)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .enabled(UPDATED_ENABLED)
            .creationTime(UPDATED_CREATION_TIME);

        restGoogleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoogleUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoogleUser))
            )
            .andExpect(status().isOk());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
        GoogleUser testGoogleUser = googleUserList.get(googleUserList.size() - 1);
        assertThat(testGoogleUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoogleUser.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
        assertThat(testGoogleUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testGoogleUser.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, googleUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(googleUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(googleUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGoogleUser() throws Exception {
        int databaseSizeBeforeUpdate = googleUserRepository.findAll().size();
        googleUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(googleUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoogleUser in the database
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGoogleUser() throws Exception {
        // Initialize the database
        googleUserRepository.saveAndFlush(googleUser);

        int databaseSizeBeforeDelete = googleUserRepository.findAll().size();

        // Delete the googleUser
        restGoogleUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, googleUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GoogleUser> googleUserList = googleUserRepository.findAll();
        assertThat(googleUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
