package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.GerdisRepository;
import com.mycompany.myapp.service.GerdisQueryService;
import com.mycompany.myapp.service.GerdisService;
import com.mycompany.myapp.service.criteria.GerdisCriteria;
import com.mycompany.myapp.service.dto.GerdisDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Gerdis}.
 */
@RestController
@RequestMapping("/api")
public class GerdisResource {

    private final Logger log = LoggerFactory.getLogger(GerdisResource.class);

    private static final String ENTITY_NAME = "gerdis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GerdisService gerdisService;

    private final GerdisRepository gerdisRepository;

    private final GerdisQueryService gerdisQueryService;

    public GerdisResource(GerdisService gerdisService, GerdisRepository gerdisRepository, GerdisQueryService gerdisQueryService) {
        this.gerdisService = gerdisService;
        this.gerdisRepository = gerdisRepository;
        this.gerdisQueryService = gerdisQueryService;
    }

    /**
     * {@code POST  /gerdis} : Create a new gerdis.
     *
     * @param gerdisDTO the gerdisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gerdisDTO, or with status {@code 400 (Bad Request)} if the gerdis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gerdis")
    public ResponseEntity<GerdisDTO> createGerdis(@Valid @RequestBody GerdisDTO gerdisDTO) throws URISyntaxException {
        log.debug("REST request to save Gerdis : {}", gerdisDTO);
        if (gerdisDTO.getId() != null) {
            throw new BadRequestAlertException("A new gerdis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GerdisDTO result = gerdisService.save(gerdisDTO);
        return ResponseEntity
            .created(new URI("/api/gerdis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gerdis/:id} : Updates an existing gerdis.
     *
     * @param id the id of the gerdisDTO to save.
     * @param gerdisDTO the gerdisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gerdisDTO,
     * or with status {@code 400 (Bad Request)} if the gerdisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gerdisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gerdis/{id}")
    public ResponseEntity<GerdisDTO> updateGerdis(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GerdisDTO gerdisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Gerdis : {}, {}", id, gerdisDTO);
        if (gerdisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gerdisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gerdisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GerdisDTO result = gerdisService.update(gerdisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gerdisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gerdis/:id} : Partial updates given fields of an existing gerdis, field will ignore if it is null
     *
     * @param id the id of the gerdisDTO to save.
     * @param gerdisDTO the gerdisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gerdisDTO,
     * or with status {@code 400 (Bad Request)} if the gerdisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gerdisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gerdisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gerdis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GerdisDTO> partialUpdateGerdis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GerdisDTO gerdisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gerdis partially : {}, {}", id, gerdisDTO);
        if (gerdisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gerdisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gerdisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GerdisDTO> result = gerdisService.partialUpdate(gerdisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gerdisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gerdis} : get all the gerdis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gerdis in body.
     */
    @GetMapping("/gerdis")
    public ResponseEntity<List<GerdisDTO>> getAllGerdis(GerdisCriteria criteria) {
        log.debug("REST request to get Gerdis by criteria: {}", criteria);
        List<GerdisDTO> entityList = gerdisQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /gerdis/count} : count all the gerdis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gerdis/count")
    public ResponseEntity<Long> countGerdis(GerdisCriteria criteria) {
        log.debug("REST request to count Gerdis by criteria: {}", criteria);
        return ResponseEntity.ok().body(gerdisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gerdis/:id} : get the "id" gerdis.
     *
     * @param id the id of the gerdisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gerdisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gerdis/{id}")
    public ResponseEntity<GerdisDTO> getGerdis(@PathVariable Long id) {
        log.debug("REST request to get Gerdis : {}", id);
        Optional<GerdisDTO> gerdisDTO = gerdisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gerdisDTO);
    }

    /**
     * {@code DELETE  /gerdis/:id} : delete the "id" gerdis.
     *
     * @param id the id of the gerdisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gerdis/{id}")
    public ResponseEntity<Void> deleteGerdis(@PathVariable Long id) {
        log.debug("REST request to delete Gerdis : {}", id);
        gerdisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
