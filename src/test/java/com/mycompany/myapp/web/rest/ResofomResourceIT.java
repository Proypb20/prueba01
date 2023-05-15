package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.domain.Resofom;
import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.repository.ResofomRepository;
import com.mycompany.myapp.service.ResofomService;
import com.mycompany.myapp.service.criteria.ResofomCriteria;
import com.mycompany.myapp.service.dto.ResofomDTO;
import com.mycompany.myapp.service.mapper.ResofomMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ResofomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResofomResourceIT {

    private static final Double DEFAULT_LIMITE_FC = 1D;
    private static final Double UPDATED_LIMITE_FC = 2D;
    private static final Double SMALLER_LIMITE_FC = 1D - 1D;

    private static final Double DEFAULT_LIMITE_FOM = 1D;
    private static final Double UPDATED_LIMITE_FOM = 2D;
    private static final Double SMALLER_LIMITE_FOM = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/resofoms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResofomRepository resofomRepository;

    @Mock
    private ResofomRepository resofomRepositoryMock;

    @Autowired
    private ResofomMapper resofomMapper;

    @Mock
    private ResofomService resofomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResofomMockMvc;

    private Resofom resofom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resofom createEntity(EntityManager em) {
        Resofom resofom = new Resofom().limite_fc(DEFAULT_LIMITE_FC).limite_fom(DEFAULT_LIMITE_FOM);
        // Add required entity
        Resolucion resolucion;
        if (TestUtil.findAll(em, Resolucion.class).isEmpty()) {
            resolucion = ResolucionResourceIT.createEntity(em);
            em.persist(resolucion);
            em.flush();
        } else {
            resolucion = TestUtil.findAll(em, Resolucion.class).get(0);
        }
        resofom.setResolucion(resolucion);
        // Add required entity
        Gerdis gerdis;
        if (TestUtil.findAll(em, Gerdis.class).isEmpty()) {
            gerdis = GerdisResourceIT.createEntity(em);
            em.persist(gerdis);
            em.flush();
        } else {
            gerdis = TestUtil.findAll(em, Gerdis.class).get(0);
        }
        resofom.setGerdis(gerdis);
        return resofom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resofom createUpdatedEntity(EntityManager em) {
        Resofom resofom = new Resofom().limite_fc(UPDATED_LIMITE_FC).limite_fom(UPDATED_LIMITE_FOM);
        // Add required entity
        Resolucion resolucion;
        if (TestUtil.findAll(em, Resolucion.class).isEmpty()) {
            resolucion = ResolucionResourceIT.createUpdatedEntity(em);
            em.persist(resolucion);
            em.flush();
        } else {
            resolucion = TestUtil.findAll(em, Resolucion.class).get(0);
        }
        resofom.setResolucion(resolucion);
        // Add required entity
        Gerdis gerdis;
        if (TestUtil.findAll(em, Gerdis.class).isEmpty()) {
            gerdis = GerdisResourceIT.createUpdatedEntity(em);
            em.persist(gerdis);
            em.flush();
        } else {
            gerdis = TestUtil.findAll(em, Gerdis.class).get(0);
        }
        resofom.setGerdis(gerdis);
        return resofom;
    }

    @BeforeEach
    public void initTest() {
        resofom = createEntity(em);
    }

    @Test
    @Transactional
    void createResofom() throws Exception {
        int databaseSizeBeforeCreate = resofomRepository.findAll().size();
        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);
        restResofomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resofomDTO)))
            .andExpect(status().isCreated());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeCreate + 1);
        Resofom testResofom = resofomList.get(resofomList.size() - 1);
        assertThat(testResofom.getLimite_fc()).isEqualTo(DEFAULT_LIMITE_FC);
        assertThat(testResofom.getLimite_fom()).isEqualTo(DEFAULT_LIMITE_FOM);
    }

    @Test
    @Transactional
    void createResofomWithExistingId() throws Exception {
        // Create the Resofom with an existing ID
        resofom.setId(1L);
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        int databaseSizeBeforeCreate = resofomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResofomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resofomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResofoms() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList
        restResofomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resofom.getId().intValue())))
            .andExpect(jsonPath("$.[*].limite_fc").value(hasItem(DEFAULT_LIMITE_FC.doubleValue())))
            .andExpect(jsonPath("$.[*].limite_fom").value(hasItem(DEFAULT_LIMITE_FOM.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResofomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(resofomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResofomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(resofomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResofomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(resofomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResofomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(resofomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getResofom() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get the resofom
        restResofomMockMvc
            .perform(get(ENTITY_API_URL_ID, resofom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resofom.getId().intValue()))
            .andExpect(jsonPath("$.limite_fc").value(DEFAULT_LIMITE_FC.doubleValue()))
            .andExpect(jsonPath("$.limite_fom").value(DEFAULT_LIMITE_FOM.doubleValue()));
    }

    @Test
    @Transactional
    void getResofomsByIdFiltering() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        Long id = resofom.getId();

        defaultResofomShouldBeFound("id.equals=" + id);
        defaultResofomShouldNotBeFound("id.notEquals=" + id);

        defaultResofomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResofomShouldNotBeFound("id.greaterThan=" + id);

        defaultResofomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResofomShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc equals to DEFAULT_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.equals=" + DEFAULT_LIMITE_FC);

        // Get all the resofomList where limite_fc equals to UPDATED_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.equals=" + UPDATED_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsInShouldWork() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc in DEFAULT_LIMITE_FC or UPDATED_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.in=" + DEFAULT_LIMITE_FC + "," + UPDATED_LIMITE_FC);

        // Get all the resofomList where limite_fc equals to UPDATED_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.in=" + UPDATED_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsNullOrNotNull() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc is not null
        defaultResofomShouldBeFound("limite_fc.specified=true");

        // Get all the resofomList where limite_fc is null
        defaultResofomShouldNotBeFound("limite_fc.specified=false");
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc is greater than or equal to DEFAULT_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.greaterThanOrEqual=" + DEFAULT_LIMITE_FC);

        // Get all the resofomList where limite_fc is greater than or equal to UPDATED_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.greaterThanOrEqual=" + UPDATED_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc is less than or equal to DEFAULT_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.lessThanOrEqual=" + DEFAULT_LIMITE_FC);

        // Get all the resofomList where limite_fc is less than or equal to SMALLER_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.lessThanOrEqual=" + SMALLER_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsLessThanSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc is less than DEFAULT_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.lessThan=" + DEFAULT_LIMITE_FC);

        // Get all the resofomList where limite_fc is less than UPDATED_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.lessThan=" + UPDATED_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fcIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fc is greater than DEFAULT_LIMITE_FC
        defaultResofomShouldNotBeFound("limite_fc.greaterThan=" + DEFAULT_LIMITE_FC);

        // Get all the resofomList where limite_fc is greater than SMALLER_LIMITE_FC
        defaultResofomShouldBeFound("limite_fc.greaterThan=" + SMALLER_LIMITE_FC);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom equals to DEFAULT_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.equals=" + DEFAULT_LIMITE_FOM);

        // Get all the resofomList where limite_fom equals to UPDATED_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.equals=" + UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsInShouldWork() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom in DEFAULT_LIMITE_FOM or UPDATED_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.in=" + DEFAULT_LIMITE_FOM + "," + UPDATED_LIMITE_FOM);

        // Get all the resofomList where limite_fom equals to UPDATED_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.in=" + UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsNullOrNotNull() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom is not null
        defaultResofomShouldBeFound("limite_fom.specified=true");

        // Get all the resofomList where limite_fom is null
        defaultResofomShouldNotBeFound("limite_fom.specified=false");
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom is greater than or equal to DEFAULT_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.greaterThanOrEqual=" + DEFAULT_LIMITE_FOM);

        // Get all the resofomList where limite_fom is greater than or equal to UPDATED_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.greaterThanOrEqual=" + UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom is less than or equal to DEFAULT_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.lessThanOrEqual=" + DEFAULT_LIMITE_FOM);

        // Get all the resofomList where limite_fom is less than or equal to SMALLER_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.lessThanOrEqual=" + SMALLER_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsLessThanSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom is less than DEFAULT_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.lessThan=" + DEFAULT_LIMITE_FOM);

        // Get all the resofomList where limite_fom is less than UPDATED_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.lessThan=" + UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByLimite_fomIsGreaterThanSomething() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        // Get all the resofomList where limite_fom is greater than DEFAULT_LIMITE_FOM
        defaultResofomShouldNotBeFound("limite_fom.greaterThan=" + DEFAULT_LIMITE_FOM);

        // Get all the resofomList where limite_fom is greater than SMALLER_LIMITE_FOM
        defaultResofomShouldBeFound("limite_fom.greaterThan=" + SMALLER_LIMITE_FOM);
    }

    @Test
    @Transactional
    void getAllResofomsByResolucionIsEqualToSomething() throws Exception {
        Resolucion resolucion;
        if (TestUtil.findAll(em, Resolucion.class).isEmpty()) {
            resofomRepository.saveAndFlush(resofom);
            resolucion = ResolucionResourceIT.createEntity(em);
        } else {
            resolucion = TestUtil.findAll(em, Resolucion.class).get(0);
        }
        em.persist(resolucion);
        em.flush();
        resofom.setResolucion(resolucion);
        resofomRepository.saveAndFlush(resofom);
        Long resolucionId = resolucion.getId();

        // Get all the resofomList where resolucion equals to resolucionId
        defaultResofomShouldBeFound("resolucionId.equals=" + resolucionId);

        // Get all the resofomList where resolucion equals to (resolucionId + 1)
        defaultResofomShouldNotBeFound("resolucionId.equals=" + (resolucionId + 1));
    }

    @Test
    @Transactional
    void getAllResofomsByGerdisIsEqualToSomething() throws Exception {
        Gerdis gerdis;
        if (TestUtil.findAll(em, Gerdis.class).isEmpty()) {
            resofomRepository.saveAndFlush(resofom);
            gerdis = GerdisResourceIT.createEntity(em);
        } else {
            gerdis = TestUtil.findAll(em, Gerdis.class).get(0);
        }
        em.persist(gerdis);
        em.flush();
        resofom.setGerdis(gerdis);
        resofomRepository.saveAndFlush(resofom);
        Long gerdisId = gerdis.getId();

        // Get all the resofomList where gerdis equals to gerdisId
        defaultResofomShouldBeFound("gerdisId.equals=" + gerdisId);

        // Get all the resofomList where gerdis equals to (gerdisId + 1)
        defaultResofomShouldNotBeFound("gerdisId.equals=" + (gerdisId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResofomShouldBeFound(String filter) throws Exception {
        restResofomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resofom.getId().intValue())))
            .andExpect(jsonPath("$.[*].limite_fc").value(hasItem(DEFAULT_LIMITE_FC.doubleValue())))
            .andExpect(jsonPath("$.[*].limite_fom").value(hasItem(DEFAULT_LIMITE_FOM.doubleValue())));

        // Check, that the count call also returns 1
        restResofomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResofomShouldNotBeFound(String filter) throws Exception {
        restResofomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResofomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResofom() throws Exception {
        // Get the resofom
        restResofomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResofom() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();

        // Update the resofom
        Resofom updatedResofom = resofomRepository.findById(resofom.getId()).get();
        // Disconnect from session so that the updates on updatedResofom are not directly saved in db
        em.detach(updatedResofom);
        updatedResofom.limite_fc(UPDATED_LIMITE_FC).limite_fom(UPDATED_LIMITE_FOM);
        ResofomDTO resofomDTO = resofomMapper.toDto(updatedResofom);

        restResofomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resofomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
        Resofom testResofom = resofomList.get(resofomList.size() - 1);
        assertThat(testResofom.getLimite_fc()).isEqualTo(UPDATED_LIMITE_FC);
        assertThat(testResofom.getLimite_fom()).isEqualTo(UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void putNonExistingResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resofomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resofomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResofomWithPatch() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();

        // Update the resofom using partial update
        Resofom partialUpdatedResofom = new Resofom();
        partialUpdatedResofom.setId(resofom.getId());

        restResofomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResofom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResofom))
            )
            .andExpect(status().isOk());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
        Resofom testResofom = resofomList.get(resofomList.size() - 1);
        assertThat(testResofom.getLimite_fc()).isEqualTo(DEFAULT_LIMITE_FC);
        assertThat(testResofom.getLimite_fom()).isEqualTo(DEFAULT_LIMITE_FOM);
    }

    @Test
    @Transactional
    void fullUpdateResofomWithPatch() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();

        // Update the resofom using partial update
        Resofom partialUpdatedResofom = new Resofom();
        partialUpdatedResofom.setId(resofom.getId());

        partialUpdatedResofom.limite_fc(UPDATED_LIMITE_FC).limite_fom(UPDATED_LIMITE_FOM);

        restResofomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResofom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResofom))
            )
            .andExpect(status().isOk());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
        Resofom testResofom = resofomList.get(resofomList.size() - 1);
        assertThat(testResofom.getLimite_fc()).isEqualTo(UPDATED_LIMITE_FC);
        assertThat(testResofom.getLimite_fom()).isEqualTo(UPDATED_LIMITE_FOM);
    }

    @Test
    @Transactional
    void patchNonExistingResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resofomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResofom() throws Exception {
        int databaseSizeBeforeUpdate = resofomRepository.findAll().size();
        resofom.setId(count.incrementAndGet());

        // Create the Resofom
        ResofomDTO resofomDTO = resofomMapper.toDto(resofom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResofomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resofomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resofom in the database
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResofom() throws Exception {
        // Initialize the database
        resofomRepository.saveAndFlush(resofom);

        int databaseSizeBeforeDelete = resofomRepository.findAll().size();

        // Delete the resofom
        restResofomMockMvc
            .perform(delete(ENTITY_API_URL_ID, resofom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resofom> resofomList = resofomRepository.findAll();
        assertThat(resofomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
