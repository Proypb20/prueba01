package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.repository.ResolucionRepository;
import com.mycompany.myapp.service.criteria.ResolucionCriteria;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import com.mycompany.myapp.service.mapper.ResolucionMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ResolucionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResolucionResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EXPEDIENTE = "AAAAAAAAAA";
    private static final String UPDATED_EXPEDIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUCION = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resolucions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResolucionRepository resolucionRepository;

    @Autowired
    private ResolucionMapper resolucionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResolucionMockMvc;

    private Resolucion resolucion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resolucion createEntity(EntityManager em) {
        Resolucion resolucion = new Resolucion().fecha(DEFAULT_FECHA).expediente(DEFAULT_EXPEDIENTE).resolucion(DEFAULT_RESOLUCION);
        return resolucion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resolucion createUpdatedEntity(EntityManager em) {
        Resolucion resolucion = new Resolucion().fecha(UPDATED_FECHA).expediente(UPDATED_EXPEDIENTE).resolucion(UPDATED_RESOLUCION);
        return resolucion;
    }

    @BeforeEach
    public void initTest() {
        resolucion = createEntity(em);
    }

    @Test
    @Transactional
    void createResolucion() throws Exception {
        int databaseSizeBeforeCreate = resolucionRepository.findAll().size();
        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);
        restResolucionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resolucionDTO)))
            .andExpect(status().isCreated());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeCreate + 1);
        Resolucion testResolucion = resolucionList.get(resolucionList.size() - 1);
        assertThat(testResolucion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testResolucion.getExpediente()).isEqualTo(DEFAULT_EXPEDIENTE);
        assertThat(testResolucion.getResolucion()).isEqualTo(DEFAULT_RESOLUCION);
    }

    @Test
    @Transactional
    void createResolucionWithExistingId() throws Exception {
        // Create the Resolucion with an existing ID
        resolucion.setId(1L);
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        int databaseSizeBeforeCreate = resolucionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResolucionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resolucionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkResolucionIsRequired() throws Exception {
        int databaseSizeBeforeTest = resolucionRepository.findAll().size();
        // set the field null
        resolucion.setResolucion(null);

        // Create the Resolucion, which fails.
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        restResolucionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resolucionDTO)))
            .andExpect(status().isBadRequest());

        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResolucions() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resolucion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].expediente").value(hasItem(DEFAULT_EXPEDIENTE)))
            .andExpect(jsonPath("$.[*].resolucion").value(hasItem(DEFAULT_RESOLUCION)));
    }

    @Test
    @Transactional
    void getResolucion() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get the resolucion
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL_ID, resolucion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resolucion.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.expediente").value(DEFAULT_EXPEDIENTE))
            .andExpect(jsonPath("$.resolucion").value(DEFAULT_RESOLUCION));
    }

    @Test
    @Transactional
    void getResolucionsByIdFiltering() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        Long id = resolucion.getId();

        defaultResolucionShouldBeFound("id.equals=" + id);
        defaultResolucionShouldNotBeFound("id.notEquals=" + id);

        defaultResolucionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResolucionShouldNotBeFound("id.greaterThan=" + id);

        defaultResolucionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResolucionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha equals to DEFAULT_FECHA
        defaultResolucionShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the resolucionList where fecha equals to UPDATED_FECHA
        defaultResolucionShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultResolucionShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the resolucionList where fecha equals to UPDATED_FECHA
        defaultResolucionShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha is not null
        defaultResolucionShouldBeFound("fecha.specified=true");

        // Get all the resolucionList where fecha is null
        defaultResolucionShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha is greater than or equal to DEFAULT_FECHA
        defaultResolucionShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the resolucionList where fecha is greater than or equal to UPDATED_FECHA
        defaultResolucionShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha is less than or equal to DEFAULT_FECHA
        defaultResolucionShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the resolucionList where fecha is less than or equal to SMALLER_FECHA
        defaultResolucionShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha is less than DEFAULT_FECHA
        defaultResolucionShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the resolucionList where fecha is less than UPDATED_FECHA
        defaultResolucionShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where fecha is greater than DEFAULT_FECHA
        defaultResolucionShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the resolucionList where fecha is greater than SMALLER_FECHA
        defaultResolucionShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllResolucionsByExpedienteIsEqualToSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where expediente equals to DEFAULT_EXPEDIENTE
        defaultResolucionShouldBeFound("expediente.equals=" + DEFAULT_EXPEDIENTE);

        // Get all the resolucionList where expediente equals to UPDATED_EXPEDIENTE
        defaultResolucionShouldNotBeFound("expediente.equals=" + UPDATED_EXPEDIENTE);
    }

    @Test
    @Transactional
    void getAllResolucionsByExpedienteIsInShouldWork() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where expediente in DEFAULT_EXPEDIENTE or UPDATED_EXPEDIENTE
        defaultResolucionShouldBeFound("expediente.in=" + DEFAULT_EXPEDIENTE + "," + UPDATED_EXPEDIENTE);

        // Get all the resolucionList where expediente equals to UPDATED_EXPEDIENTE
        defaultResolucionShouldNotBeFound("expediente.in=" + UPDATED_EXPEDIENTE);
    }

    @Test
    @Transactional
    void getAllResolucionsByExpedienteIsNullOrNotNull() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where expediente is not null
        defaultResolucionShouldBeFound("expediente.specified=true");

        // Get all the resolucionList where expediente is null
        defaultResolucionShouldNotBeFound("expediente.specified=false");
    }

    @Test
    @Transactional
    void getAllResolucionsByExpedienteContainsSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where expediente contains DEFAULT_EXPEDIENTE
        defaultResolucionShouldBeFound("expediente.contains=" + DEFAULT_EXPEDIENTE);

        // Get all the resolucionList where expediente contains UPDATED_EXPEDIENTE
        defaultResolucionShouldNotBeFound("expediente.contains=" + UPDATED_EXPEDIENTE);
    }

    @Test
    @Transactional
    void getAllResolucionsByExpedienteNotContainsSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where expediente does not contain DEFAULT_EXPEDIENTE
        defaultResolucionShouldNotBeFound("expediente.doesNotContain=" + DEFAULT_EXPEDIENTE);

        // Get all the resolucionList where expediente does not contain UPDATED_EXPEDIENTE
        defaultResolucionShouldBeFound("expediente.doesNotContain=" + UPDATED_EXPEDIENTE);
    }

    @Test
    @Transactional
    void getAllResolucionsByResolucionIsEqualToSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where resolucion equals to DEFAULT_RESOLUCION
        defaultResolucionShouldBeFound("resolucion.equals=" + DEFAULT_RESOLUCION);

        // Get all the resolucionList where resolucion equals to UPDATED_RESOLUCION
        defaultResolucionShouldNotBeFound("resolucion.equals=" + UPDATED_RESOLUCION);
    }

    @Test
    @Transactional
    void getAllResolucionsByResolucionIsInShouldWork() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where resolucion in DEFAULT_RESOLUCION or UPDATED_RESOLUCION
        defaultResolucionShouldBeFound("resolucion.in=" + DEFAULT_RESOLUCION + "," + UPDATED_RESOLUCION);

        // Get all the resolucionList where resolucion equals to UPDATED_RESOLUCION
        defaultResolucionShouldNotBeFound("resolucion.in=" + UPDATED_RESOLUCION);
    }

    @Test
    @Transactional
    void getAllResolucionsByResolucionIsNullOrNotNull() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where resolucion is not null
        defaultResolucionShouldBeFound("resolucion.specified=true");

        // Get all the resolucionList where resolucion is null
        defaultResolucionShouldNotBeFound("resolucion.specified=false");
    }

    @Test
    @Transactional
    void getAllResolucionsByResolucionContainsSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where resolucion contains DEFAULT_RESOLUCION
        defaultResolucionShouldBeFound("resolucion.contains=" + DEFAULT_RESOLUCION);

        // Get all the resolucionList where resolucion contains UPDATED_RESOLUCION
        defaultResolucionShouldNotBeFound("resolucion.contains=" + UPDATED_RESOLUCION);
    }

    @Test
    @Transactional
    void getAllResolucionsByResolucionNotContainsSomething() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        // Get all the resolucionList where resolucion does not contain DEFAULT_RESOLUCION
        defaultResolucionShouldNotBeFound("resolucion.doesNotContain=" + DEFAULT_RESOLUCION);

        // Get all the resolucionList where resolucion does not contain UPDATED_RESOLUCION
        defaultResolucionShouldBeFound("resolucion.doesNotContain=" + UPDATED_RESOLUCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResolucionShouldBeFound(String filter) throws Exception {
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resolucion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].expediente").value(hasItem(DEFAULT_EXPEDIENTE)))
            .andExpect(jsonPath("$.[*].resolucion").value(hasItem(DEFAULT_RESOLUCION)));

        // Check, that the count call also returns 1
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResolucionShouldNotBeFound(String filter) throws Exception {
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResolucionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResolucion() throws Exception {
        // Get the resolucion
        restResolucionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResolucion() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();

        // Update the resolucion
        Resolucion updatedResolucion = resolucionRepository.findById(resolucion.getId()).get();
        // Disconnect from session so that the updates on updatedResolucion are not directly saved in db
        em.detach(updatedResolucion);
        updatedResolucion.fecha(UPDATED_FECHA).expediente(UPDATED_EXPEDIENTE).resolucion(UPDATED_RESOLUCION);
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(updatedResolucion);

        restResolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resolucionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
        Resolucion testResolucion = resolucionList.get(resolucionList.size() - 1);
        assertThat(testResolucion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testResolucion.getExpediente()).isEqualTo(UPDATED_EXPEDIENTE);
        assertThat(testResolucion.getResolucion()).isEqualTo(UPDATED_RESOLUCION);
    }

    @Test
    @Transactional
    void putNonExistingResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resolucionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resolucionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResolucionWithPatch() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();

        // Update the resolucion using partial update
        Resolucion partialUpdatedResolucion = new Resolucion();
        partialUpdatedResolucion.setId(resolucion.getId());

        restResolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResolucion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResolucion))
            )
            .andExpect(status().isOk());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
        Resolucion testResolucion = resolucionList.get(resolucionList.size() - 1);
        assertThat(testResolucion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testResolucion.getExpediente()).isEqualTo(DEFAULT_EXPEDIENTE);
        assertThat(testResolucion.getResolucion()).isEqualTo(DEFAULT_RESOLUCION);
    }

    @Test
    @Transactional
    void fullUpdateResolucionWithPatch() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();

        // Update the resolucion using partial update
        Resolucion partialUpdatedResolucion = new Resolucion();
        partialUpdatedResolucion.setId(resolucion.getId());

        partialUpdatedResolucion.fecha(UPDATED_FECHA).expediente(UPDATED_EXPEDIENTE).resolucion(UPDATED_RESOLUCION);

        restResolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResolucion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResolucion))
            )
            .andExpect(status().isOk());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
        Resolucion testResolucion = resolucionList.get(resolucionList.size() - 1);
        assertThat(testResolucion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testResolucion.getExpediente()).isEqualTo(UPDATED_EXPEDIENTE);
        assertThat(testResolucion.getResolucion()).isEqualTo(UPDATED_RESOLUCION);
    }

    @Test
    @Transactional
    void patchNonExistingResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resolucionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResolucion() throws Exception {
        int databaseSizeBeforeUpdate = resolucionRepository.findAll().size();
        resolucion.setId(count.incrementAndGet());

        // Create the Resolucion
        ResolucionDTO resolucionDTO = resolucionMapper.toDto(resolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResolucionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resolucionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resolucion in the database
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResolucion() throws Exception {
        // Initialize the database
        resolucionRepository.saveAndFlush(resolucion);

        int databaseSizeBeforeDelete = resolucionRepository.findAll().size();

        // Delete the resolucion
        restResolucionMockMvc
            .perform(delete(ENTITY_API_URL_ID, resolucion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resolucion> resolucionList = resolucionRepository.findAll();
        assertThat(resolucionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
