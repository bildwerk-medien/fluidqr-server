package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.FluidQrServerApp;
import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.domain.User;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.repository.UserRepository;
import de.bildwerkmedien.fluidqr.server.service.QrCodeQueryService;
import de.bildwerkmedien.fluidqr.server.service.QrCodeService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QrCodeResource} REST controller.
 */
@SpringBootTest(classes = FluidQrServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class QrCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QrCodeQueryService qrCodeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQrCodeMockMvc;

    private QrCode qrCode;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCode createEntity(EntityManager em) {
        QrCode qrCode = new QrCode()
            .code(DEFAULT_CODE)
            .user(UserResourceIT.create());
        return qrCode;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCode createUpdatedEntity(EntityManager em) {
        QrCode qrCode = new QrCode()
            .code(UPDATED_CODE);
        return qrCode;
    }

    @BeforeEach
    public void initTest() {
        qrCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createQrCode() throws Exception {
        int databaseSizeBeforeCreate = qrCodeRepository.findAll().size();
        // Create the QrCode
        restQrCodeMockMvc.perform(post("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isCreated());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeCreate + 1);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createQrCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qrCodeRepository.findAll().size();

        // Create the QrCode with an existing ID
        qrCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQrCodeMockMvc.perform(post("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrCodeRepository.findAll().size();
        // set the field null
        qrCode.setCode(null);

        // Create the QrCode, which fails.


        restQrCodeMockMvc.perform(post("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isBadRequest());

        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQrCodes() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList
        restQrCodeMockMvc.perform(get("/api/qr-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    public void getQrCode() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get the qrCode
        restQrCodeMockMvc.perform(get("/api/qr-codes/{id}", qrCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qrCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getQrCodeByIdForOwnUserOnly() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        qrCode.setUser(user);
        qrCodeRepository.saveAndFlush(qrCode);

        // Get the qrCode
        restQrCodeMockMvc.perform(get("/api/qr-codes/{id}", qrCode.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getQrCodeForOwnUserOnly() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        qrCode.setUser(user);
        qrCodeRepository.saveAndFlush(qrCode);

        // Get the qrCode
        restQrCodeMockMvc.perform(get("/api/qr-codes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(empty())));
    }


    @Test
    @Transactional
    public void getQrCodesByIdFiltering() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        Long id = qrCode.getId();

        defaultQrCodeShouldBeFound("id.equals=" + id);
        defaultQrCodeShouldNotBeFound("id.notEquals=" + id);

        defaultQrCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQrCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultQrCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQrCodeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQrCodesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code equals to DEFAULT_CODE
        defaultQrCodeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the qrCodeList where code equals to UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrCodesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code not equals to DEFAULT_CODE
        defaultQrCodeShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the qrCodeList where code not equals to UPDATED_CODE
        defaultQrCodeShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrCodesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultQrCodeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the qrCodeList where code equals to UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrCodesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code is not null
        defaultQrCodeShouldBeFound("code.specified=true");

        // Get all the qrCodeList where code is null
        defaultQrCodeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrCodesByCodeContainsSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code contains DEFAULT_CODE
        defaultQrCodeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the qrCodeList where code contains UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrCodesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code does not contain DEFAULT_CODE
        defaultQrCodeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the qrCodeList where code does not contain UPDATED_CODE
        defaultQrCodeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllQrCodesByRedirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);
        Redirection redirection = RedirectionResourceIT.createEntity(em);
        em.persist(redirection);
        em.flush();
        qrCode.addRedirection(redirection);
        qrCodeRepository.saveAndFlush(qrCode);
        Long redirectionId = redirection.getId();

        // Get all the qrCodeList where redirection equals to redirectionId
        defaultQrCodeShouldBeFound("redirectionId.equals=" + redirectionId);

        // Get all the qrCodeList where redirection equals to redirectionId + 1
        defaultQrCodeShouldNotBeFound("redirectionId.equals=" + (redirectionId + 1));
    }


    @Test
    @Transactional
    public void getAllQrCodesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);
        User user = UserResourceIT.create();
        qrCode.setUser(user);
        qrCodeRepository.saveAndFlush(qrCode);
        Long userId = user.getId();

        // Get all the qrCodeList where user equals to userId
        defaultQrCodeShouldBeFound("userId.equals=" + userId);

        // Get all the qrCodeList where user equals to userId + 1
        defaultQrCodeShouldBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQrCodeShouldBeFound(String filter) throws Exception {
        restQrCodeMockMvc.perform(get("/api/qr-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restQrCodeMockMvc.perform(get("/api/qr-codes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQrCodeShouldNotBeFound(String filter) throws Exception {
        restQrCodeMockMvc.perform(get("/api/qr-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQrCodeMockMvc.perform(get("/api/qr-codes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQrCode() throws Exception {
        // Get the qrCode
        restQrCodeMockMvc.perform(get("/api/qr-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQrCode() throws Exception {
        // Initialize the database
        qrCodeService.save(qrCode);

        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();

        // Update the qrCode
        QrCode updatedQrCode = qrCodeRepository.findById(qrCode.getId()).get();
        // Disconnect from session so that the updates on updatedQrCode are not directly saved in db
        em.detach(updatedQrCode);
        updatedQrCode
            .code(UPDATED_CODE);

        restQrCodeMockMvc.perform(put("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQrCode)))
            .andExpect(status().isOk());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateQrCodeWithAnotherUserIsFailing() throws Exception {

        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        qrCode.setUser(user);
        qrCodeRepository.save(qrCode);

        // Update the qrCode
        QrCode updatedQrCode = qrCodeRepository.findById(qrCode.getId()).get();
        // Disconnect from session so that the updates on updatedQrCode are not directly saved in db
        em.detach(updatedQrCode);

        updatedQrCode
            .code(UPDATED_CODE);

        restQrCodeMockMvc.perform(put("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQrCode)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void updateNonExistingQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrCodeMockMvc.perform(put("/api/qr-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQrCode() throws Exception {
        // Initialize the database
        qrCodeService.save(qrCode);

        int databaseSizeBeforeDelete = qrCodeRepository.findAll().size();

        // Delete the qrCode
        restQrCodeMockMvc.perform(delete("/api/qr-codes/{id}", qrCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void doNotDeleteQrCodeFromOtherUser() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        userRepository.saveAndFlush(user);
        qrCode.setUser(user);
        qrCodeRepository.save(qrCode);

        int databaseSizeBeforeDelete = qrCodeRepository.findAll().size();

        // Delete the qrCode
        restQrCodeMockMvc.perform(delete("/api/qr-codes/{id}", qrCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeDelete);
    }
}
