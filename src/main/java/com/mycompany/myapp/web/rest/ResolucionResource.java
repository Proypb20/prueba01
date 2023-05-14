package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ResolucionRepository;
import com.mycompany.myapp.service.ResolucionQueryService;
import com.mycompany.myapp.service.ResolucionService;
import com.mycompany.myapp.service.criteria.ResolucionCriteria;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Resolucion}.
 */
@RestController
@RequestMapping("/api")
public class ResolucionResource {

    private final Logger log = LoggerFactory.getLogger(ResolucionResource.class);

    private static final String ENTITY_NAME = "resolucion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResolucionService resolucionService;

    private final ResolucionRepository resolucionRepository;

    private final ResolucionQueryService resolucionQueryService;

    public ResolucionResource(
        ResolucionService resolucionService,
        ResolucionRepository resolucionRepository,
        ResolucionQueryService resolucionQueryService
    ) {
        this.resolucionService = resolucionService;
        this.resolucionRepository = resolucionRepository;
        this.resolucionQueryService = resolucionQueryService;
    }

    /**
     * {@code POST  /resolucions} : Create a new resolucion.
     *
     * @param resolucionDTO the resolucionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resolucionDTO, or with status {@code 400 (Bad Request)} if the resolucion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resolucions")
    public ResponseEntity<ResolucionDTO> createResolucion(@Valid @RequestBody ResolucionDTO resolucionDTO) throws URISyntaxException {
        log.debug("REST request to save Resolucion : {}", resolucionDTO);
        if (resolucionDTO.getId() != null) {
            throw new BadRequestAlertException("A new resolucion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResolucionDTO result = resolucionService.save(resolucionDTO);
        return ResponseEntity
            .created(new URI("/api/resolucions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resolucions/:id} : Updates an existing resolucion.
     *
     * @param id the id of the resolucionDTO to save.
     * @param resolucionDTO the resolucionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resolucionDTO,
     * or with status {@code 400 (Bad Request)} if the resolucionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resolucionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resolucions/{id}")
    public ResponseEntity<ResolucionDTO> updateResolucion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResolucionDTO resolucionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resolucion : {}, {}", id, resolucionDTO);
        if (resolucionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resolucionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resolucionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResolucionDTO result = resolucionService.update(resolucionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resolucionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resolucions/:id} : Partial updates given fields of an existing resolucion, field will ignore if it is null
     *
     * @param id the id of the resolucionDTO to save.
     * @param resolucionDTO the resolucionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resolucionDTO,
     * or with status {@code 400 (Bad Request)} if the resolucionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resolucionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resolucionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resolucions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResolucionDTO> partialUpdateResolucion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResolucionDTO resolucionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resolucion partially : {}, {}", id, resolucionDTO);
        if (resolucionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resolucionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resolucionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResolucionDTO> result = resolucionService.partialUpdate(resolucionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resolucionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resolucions} : get all the resolucions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resolucions in body.
     */
    @GetMapping("/resolucions")
    public ResponseEntity<List<ResolucionDTO>> getAllResolucions(ResolucionCriteria criteria) {
        log.debug("REST request to get Resolucions by criteria: {}", criteria);
        List<ResolucionDTO> entityList = resolucionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /resolucions/count} : count all the resolucions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/resolucions/count")
    public ResponseEntity<Long> countResolucions(ResolucionCriteria criteria) {
        log.debug("REST request to count Resolucions by criteria: {}", criteria);
        return ResponseEntity.ok().body(resolucionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /resolucions/:id} : get the "id" resolucion.
     *
     * @param id the id of the resolucionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resolucionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resolucions/{id}")
    public ResponseEntity<ResolucionDTO> getResolucion(@PathVariable Long id) {
        log.debug("REST request to get Resolucion : {}", id);
        Optional<ResolucionDTO> resolucionDTO = resolucionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resolucionDTO);
    }

    /**
     * {@code DELETE  /resolucions/:id} : delete the "id" resolucion.
     *
     * @param id the id of the resolucionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resolucions/{id}")
    public ResponseEntity<Void> deleteResolucion(@PathVariable Long id) {
        log.debug("REST request to delete Resolucion : {}", id);
        resolucionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
