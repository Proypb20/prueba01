package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ResofomRepository;
import com.mycompany.myapp.service.ResofomQueryService;
import com.mycompany.myapp.service.ResofomService;
import com.mycompany.myapp.service.criteria.ResofomCriteria;
import com.mycompany.myapp.service.dto.ResofomDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Resofom}.
 */
@RestController
@RequestMapping("/api")
public class ResofomResource {

    private final Logger log = LoggerFactory.getLogger(ResofomResource.class);

    private static final String ENTITY_NAME = "resofom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResofomService resofomService;

    private final ResofomRepository resofomRepository;

    private final ResofomQueryService resofomQueryService;

    public ResofomResource(ResofomService resofomService, ResofomRepository resofomRepository, ResofomQueryService resofomQueryService) {
        this.resofomService = resofomService;
        this.resofomRepository = resofomRepository;
        this.resofomQueryService = resofomQueryService;
    }

    /**
     * {@code POST  /resofoms} : Create a new resofom.
     *
     * @param resofomDTO the resofomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resofomDTO, or with status {@code 400 (Bad Request)} if the resofom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resofoms")
    public ResponseEntity<ResofomDTO> createResofom(@Valid @RequestBody ResofomDTO resofomDTO) throws URISyntaxException {
        log.debug("REST request to save Resofom : {}", resofomDTO);
        if (resofomDTO.getId() != null) {
            throw new BadRequestAlertException("A new resofom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResofomDTO result = resofomService.save(resofomDTO);
        return ResponseEntity
            .created(new URI("/api/resofoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resofoms/:id} : Updates an existing resofom.
     *
     * @param id the id of the resofomDTO to save.
     * @param resofomDTO the resofomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resofomDTO,
     * or with status {@code 400 (Bad Request)} if the resofomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resofomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resofoms/{id}")
    public ResponseEntity<ResofomDTO> updateResofom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResofomDTO resofomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resofom : {}, {}", id, resofomDTO);
        if (resofomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resofomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resofomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResofomDTO result = resofomService.update(resofomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resofomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resofoms/:id} : Partial updates given fields of an existing resofom, field will ignore if it is null
     *
     * @param id the id of the resofomDTO to save.
     * @param resofomDTO the resofomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resofomDTO,
     * or with status {@code 400 (Bad Request)} if the resofomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resofomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resofomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resofoms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResofomDTO> partialUpdateResofom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResofomDTO resofomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resofom partially : {}, {}", id, resofomDTO);
        if (resofomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resofomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resofomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResofomDTO> result = resofomService.partialUpdate(resofomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resofomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resofoms} : get all the resofoms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resofoms in body.
     */
    @GetMapping("/resofoms")
    public ResponseEntity<List<ResofomDTO>> getAllResofoms(ResofomCriteria criteria) {
        log.debug("REST request to get Resofoms by criteria: {}", criteria);
        List<ResofomDTO> entityList = resofomQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /resofoms/count} : count all the resofoms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/resofoms/count")
    public ResponseEntity<Long> countResofoms(ResofomCriteria criteria) {
        log.debug("REST request to count Resofoms by criteria: {}", criteria);
        return ResponseEntity.ok().body(resofomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /resofoms/:id} : get the "id" resofom.
     *
     * @param id the id of the resofomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resofomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resofoms/{id}")
    public ResponseEntity<ResofomDTO> getResofom(@PathVariable Long id) {
        log.debug("REST request to get Resofom : {}", id);
        Optional<ResofomDTO> resofomDTO = resofomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resofomDTO);
    }

    /**
     * {@code DELETE  /resofoms/:id} : delete the "id" resofom.
     *
     * @param id the id of the resofomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resofoms/{id}")
    public ResponseEntity<Void> deleteResofom(@PathVariable Long id) {
        log.debug("REST request to delete Resofom : {}", id);
        resofomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
