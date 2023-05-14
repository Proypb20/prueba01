package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.repository.ResolucionRepository;
import com.mycompany.myapp.service.criteria.ResolucionCriteria;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import com.mycompany.myapp.service.mapper.ResolucionMapper;
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
 * Service for executing complex queries for {@link Resolucion} entities in the database.
 * The main input is a {@link ResolucionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResolucionDTO} or a {@link Page} of {@link ResolucionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResolucionQueryService extends QueryService<Resolucion> {

    private final Logger log = LoggerFactory.getLogger(ResolucionQueryService.class);

    private final ResolucionRepository resolucionRepository;

    private final ResolucionMapper resolucionMapper;

    public ResolucionQueryService(ResolucionRepository resolucionRepository, ResolucionMapper resolucionMapper) {
        this.resolucionRepository = resolucionRepository;
        this.resolucionMapper = resolucionMapper;
    }

    /**
     * Return a {@link List} of {@link ResolucionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResolucionDTO> findByCriteria(ResolucionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Resolucion> specification = createSpecification(criteria);
        return resolucionMapper.toDto(resolucionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResolucionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResolucionDTO> findByCriteria(ResolucionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Resolucion> specification = createSpecification(criteria);
        return resolucionRepository.findAll(specification, page).map(resolucionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResolucionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Resolucion> specification = createSpecification(criteria);
        return resolucionRepository.count(specification);
    }

    /**
     * Function to convert {@link ResolucionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Resolucion> createSpecification(ResolucionCriteria criteria) {
        Specification<Resolucion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Resolucion_.id));
            }
            if (criteria.getResolucion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResolucion(), Resolucion_.resolucion));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Resolucion_.fecha));
            }
            if (criteria.getExpediente() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpediente(), Resolucion_.expediente));
            }
            if (criteria.getResolucionb() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResolucionb(), Resolucion_.resolucionb));
            }
        }
        return specification;
    }
}
