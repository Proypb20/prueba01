package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.repository.GerdisRepository;
import com.mycompany.myapp.service.criteria.GerdisCriteria;
import com.mycompany.myapp.service.dto.GerdisDTO;
import com.mycompany.myapp.service.mapper.GerdisMapper;
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
 * Integration tests for the {@link GerdisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GerdisResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gerdis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GerdisRepository gerdisRepository;

    @Autowired
    private GerdisMapper gerdisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGerdisMockMvc;

    private Gerdis gerdis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gerdis createEntity(EntityManager em) {
        Gerdis gerdis = new Gerdis().descripcion(DEFAULT_DESCRIPCION);
        return gerdis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gerdis createUpdatedEntity(EntityManager em) {
        Gerdis gerdis = new Gerdis().descripcion(UPDATED_DESCRIPCION);
        return gerdis;
    }

    @BeforeEach
    public void initTest() {
        gerdis = createEntity(em);
    }

    @Test
    @Transactional
    void createGerdis() throws Exception {
        int databaseSizeBeforeCreate = gerdisRepository.findAll().size();
        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);
        restGerdisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gerdisDTO)))
            .andExpect(status().isCreated());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeCreate + 1);
        Gerdis testGerdis = gerdisList.get(gerdisList.size() - 1);
        assertThat(testGerdis.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createGerdisWithExistingId() throws Exception {
        // Create the Gerdis with an existing ID
        gerdis.setId(1L);
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        int databaseSizeBeforeCreate = gerdisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGerdisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gerdisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGerdis() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gerdis.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getGerdis() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get the gerdis
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL_ID, gerdis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gerdis.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getGerdisByIdFiltering() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        Long id = gerdis.getId();

        defaultGerdisShouldBeFound("id.equals=" + id);
        defaultGerdisShouldNotBeFound("id.notEquals=" + id);

        defaultGerdisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGerdisShouldNotBeFound("id.greaterThan=" + id);

        defaultGerdisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGerdisShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGerdisByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList where descripcion equals to DEFAULT_DESCRIPCION
        defaultGerdisShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the gerdisList where descripcion equals to UPDATED_DESCRIPCION
        defaultGerdisShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGerdisByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultGerdisShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the gerdisList where descripcion equals to UPDATED_DESCRIPCION
        defaultGerdisShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGerdisByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList where descripcion is not null
        defaultGerdisShouldBeFound("descripcion.specified=true");

        // Get all the gerdisList where descripcion is null
        defaultGerdisShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllGerdisByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList where descripcion contains DEFAULT_DESCRIPCION
        defaultGerdisShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the gerdisList where descripcion contains UPDATED_DESCRIPCION
        defaultGerdisShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGerdisByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        // Get all the gerdisList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultGerdisShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the gerdisList where descripcion does not contain UPDATED_DESCRIPCION
        defaultGerdisShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGerdisShouldBeFound(String filter) throws Exception {
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gerdis.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGerdisShouldNotBeFound(String filter) throws Exception {
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGerdisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGerdis() throws Exception {
        // Get the gerdis
        restGerdisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGerdis() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();

        // Update the gerdis
        Gerdis updatedGerdis = gerdisRepository.findById(gerdis.getId()).get();
        // Disconnect from session so that the updates on updatedGerdis are not directly saved in db
        em.detach(updatedGerdis);
        updatedGerdis.descripcion(UPDATED_DESCRIPCION);
        GerdisDTO gerdisDTO = gerdisMapper.toDto(updatedGerdis);

        restGerdisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gerdisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
        Gerdis testGerdis = gerdisList.get(gerdisList.size() - 1);
        assertThat(testGerdis.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gerdisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gerdisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGerdisWithPatch() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();

        // Update the gerdis using partial update
        Gerdis partialUpdatedGerdis = new Gerdis();
        partialUpdatedGerdis.setId(gerdis.getId());

        partialUpdatedGerdis.descripcion(UPDATED_DESCRIPCION);

        restGerdisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGerdis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGerdis))
            )
            .andExpect(status().isOk());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
        Gerdis testGerdis = gerdisList.get(gerdisList.size() - 1);
        assertThat(testGerdis.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateGerdisWithPatch() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();

        // Update the gerdis using partial update
        Gerdis partialUpdatedGerdis = new Gerdis();
        partialUpdatedGerdis.setId(gerdis.getId());

        partialUpdatedGerdis.descripcion(UPDATED_DESCRIPCION);

        restGerdisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGerdis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGerdis))
            )
            .andExpect(status().isOk());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
        Gerdis testGerdis = gerdisList.get(gerdisList.size() - 1);
        assertThat(testGerdis.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gerdisDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGerdis() throws Exception {
        int databaseSizeBeforeUpdate = gerdisRepository.findAll().size();
        gerdis.setId(count.incrementAndGet());

        // Create the Gerdis
        GerdisDTO gerdisDTO = gerdisMapper.toDto(gerdis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGerdisMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gerdisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gerdis in the database
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGerdis() throws Exception {
        // Initialize the database
        gerdisRepository.saveAndFlush(gerdis);

        int databaseSizeBeforeDelete = gerdisRepository.findAll().size();

        // Delete the gerdis
        restGerdisMockMvc
            .perform(delete(ENTITY_API_URL_ID, gerdis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gerdis> gerdisList = gerdisRepository.findAll();
        assertThat(gerdisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
