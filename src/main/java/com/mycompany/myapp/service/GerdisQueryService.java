package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.repository.GerdisRepository;
import com.mycompany.myapp.service.criteria.GerdisCriteria;
import com.mycompany.myapp.service.dto.GerdisDTO;
import com.mycompany.myapp.service.mapper.GerdisMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Gerdis} entities in the database.
 * The main input is a {@link GerdisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GerdisDTO} or a {@link Page} of {@link GerdisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GerdisQueryService extends QueryService<Gerdis> {

    private final Logger log = LoggerFactory.getLogger(GerdisQueryService.class);

    private final GerdisRepository gerdisRepository;

    private final GerdisMapper gerdisMapper;

    public GerdisQueryService(GerdisRepository gerdisRepository, GerdisMapper gerdisMapper) {
        this.gerdisRepository = gerdisRepository;
        this.gerdisMapper = gerdisMapper;
    }

    /**
     * Return a {@link List} of {@link GerdisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GerdisDTO> findByCriteria(GerdisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gerdis> specification = createSpecification(criteria);
        return gerdisMapper.toDto(gerdisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GerdisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GerdisDTO> findByCriteria(GerdisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gerdis> specification = createSpecification(criteria);
        return gerdisRepository.findAll(specification, page).map(gerdisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GerdisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gerdis> specification = createSpecification(criteria);
        return gerdisRepository.count(specification);
    }

    /**
     * Function to convert {@link GerdisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gerdis> createSpecification(GerdisCriteria criteria) {
        Specification<Gerdis> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gerdis_.id));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Gerdis_.descripcion));
            }
        }
        return specification;
    }
}
