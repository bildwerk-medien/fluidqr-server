package de.bildwerkmedien.fluidqr.server.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerkmedien.fluidqr.server.IntegrationTest;
import de.bildwerkmedien.fluidqr.server.domain.QrCode;
import de.bildwerkmedien.fluidqr.server.domain.Redirection;
import de.bildwerkmedien.fluidqr.server.domain.User;
import de.bildwerkmedien.fluidqr.server.repository.QrCodeRepository;
import de.bildwerkmedien.fluidqr.server.repository.UserRepository;
import de.bildwerkmedien.fluidqr.server.service.criteria.QrCodeCriteria;
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
 * Integration tests for the {@link QrCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QrCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/qr-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQrCodeMockMvc;

    private QrCode qrCode;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCode createEntity(EntityManager em, UserRepository userRepository) {
        QrCode qrCode = new QrCode().code(DEFAULT_CODE).user(userRepository.getById(2L));
        return qrCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCode createUpdatedEntity(EntityManager em) {
        QrCode qrCode = new QrCode().code(UPDATED_CODE);
        return qrCode;
    }

    @BeforeEach
    public void initTest() {
        qrCode = createEntity(em, userRepository);
    }

    @Test
    @Transactional
    void createQrCode() throws Exception {
        int databaseSizeBeforeCreate = qrCodeRepository.findAll().size();
        // Create the QrCode
        restQrCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isCreated());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeCreate + 1);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createQrCodeWithExistingId() throws Exception {
        // Create the QrCode with an existing ID
        qrCode.setId(1L);

        int databaseSizeBeforeCreate = qrCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQrCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrCodeRepository.findAll().size();
        // set the field null
        qrCode.setCode(null);

        // Create the QrCode, which fails.

        restQrCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isBadRequest());

        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQrCodes() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getQrCode() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get the qrCode
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, qrCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qrCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getQrCodesByIdFiltering() throws Exception {
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
    void getAllQrCodesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code equals to DEFAULT_CODE
        defaultQrCodeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the qrCodeList where code equals to UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllQrCodesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code not equals to DEFAULT_CODE
        defaultQrCodeShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the qrCodeList where code not equals to UPDATED_CODE
        defaultQrCodeShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllQrCodesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultQrCodeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the qrCodeList where code equals to UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllQrCodesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code is not null
        defaultQrCodeShouldBeFound("code.specified=true");

        // Get all the qrCodeList where code is null
        defaultQrCodeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodesByCodeContainsSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code contains DEFAULT_CODE
        defaultQrCodeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the qrCodeList where code contains UPDATED_CODE
        defaultQrCodeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllQrCodesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where code does not contain DEFAULT_CODE
        defaultQrCodeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the qrCodeList where code does not contain UPDATED_CODE
        defaultQrCodeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllQrCodesByRedirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);
        Redirection redirection;
        if (TestUtil.findAll(em, Redirection.class).isEmpty()) {
            redirection = RedirectionResourceIT.createEntity(em, userRepository);
            em.persist(redirection);
            em.flush();
        } else {
            redirection = TestUtil.findAll(em, Redirection.class).get(0);
        }
        em.persist(redirection);
        em.flush();
        qrCode.addRedirection(redirection);
        qrCodeRepository.saveAndFlush(qrCode);
        Long redirectionId = redirection.getId();

        // Get all the qrCodeList where redirection equals to redirectionId
        defaultQrCodeShouldBeFound("redirectionId.equals=" + redirectionId);

        // Get all the qrCodeList where redirection equals to (redirectionId + 1)
        defaultQrCodeShouldNotBeFound("redirectionId.equals=" + (redirectionId + 1));
    }

    @Test
    @Transactional
    void getAllQrCodesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        // Get all the qrCodeList where user equals to 2
        defaultQrCodeShouldBeFound("userId.equals=2");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQrCodeShouldBeFound(String filter) throws Exception {
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQrCodeShouldNotBeFound(String filter) throws Exception {
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQrCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQrCode() throws Exception {
        // Get the qrCode
        restQrCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQrCode() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();

        // Update the qrCode
        QrCode updatedQrCode = qrCodeRepository.findById(qrCode.getId()).get();
        // Disconnect from session so that the updates on updatedQrCode are not directly saved in db
        em.detach(updatedQrCode);
        updatedQrCode.code(UPDATED_CODE);

        restQrCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQrCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQrCode))
            )
            .andExpect(status().isOk());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qrCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQrCodeWithPatch() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();

        // Update the qrCode using partial update
        QrCode partialUpdatedQrCode = new QrCode();
        partialUpdatedQrCode.setId(qrCode.getId());

        restQrCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQrCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQrCode))
            )
            .andExpect(status().isOk());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateQrCodeWithPatch() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();

        // Update the qrCode using partial update
        QrCode partialUpdatedQrCode = new QrCode();
        partialUpdatedQrCode.setId(qrCode.getId());

        partialUpdatedQrCode.code(UPDATED_CODE);

        restQrCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQrCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQrCode))
            )
            .andExpect(status().isOk());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
        QrCode testQrCode = qrCodeList.get(qrCodeList.size() - 1);
        assertThat(testQrCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qrCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQrCode() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeRepository.findAll().size();
        qrCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(qrCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QrCode in the database
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQrCode() throws Exception {
        // Initialize the database
        qrCodeRepository.saveAndFlush(qrCode);

        int databaseSizeBeforeDelete = qrCodeRepository.findAll().size();

        // Delete the qrCode
        restQrCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, qrCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QrCode> qrCodeList = qrCodeRepository.findAll();
        assertThat(qrCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
