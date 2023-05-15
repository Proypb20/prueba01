package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Resofom;
import com.mycompany.myapp.repository.ResofomRepository;
import com.mycompany.myapp.service.criteria.ResofomCriteria;
import com.mycompany.myapp.service.dto.ResofomDTO;
import com.mycompany.myapp.service.mapper.ResofomMapper;
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
 * Service for executing complex queries for {@link Resofom} entities in the database.
 * The main input is a {@link ResofomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResofomDTO} or a {@link Page} of {@link ResofomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResofomQueryService extends QueryService<Resofom> {

    private final Logger log = LoggerFactory.getLogger(ResofomQueryService.class);

    private final ResofomRepository resofomRepository;

    private final ResofomMapper resofomMapper;

    public ResofomQueryService(ResofomRepository resofomRepository, ResofomMapper resofomMapper) {
        this.resofomRepository = resofomRepository;
        this.resofomMapper = resofomMapper;
    }

    /**
     * Return a {@link List} of {@link ResofomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResofomDTO> findByCriteria(ResofomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Resofom> specification = createSpecification(criteria);
        return resofomMapper.toDto(resofomRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResofomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResofomDTO> findByCriteria(ResofomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Resofom> specification = createSpecification(criteria);
        return resofomRepository.findAll(specification, page).map(resofomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResofomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Resofom> specification = createSpecification(criteria);
        return resofomRepository.count(specification);
    }

    /**
     * Function to convert {@link ResofomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Resofom> createSpecification(ResofomCriteria criteria) {
        Specification<Resofom> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Resofom_.id));
            }
            if (criteria.getLimite_fc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLimite_fc(), Resofom_.limite_fc));
            }
            if (criteria.getLimite_fom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLimite_fom(), Resofom_.limite_fom));
            }
            if (criteria.getResolucionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getResolucionId(),
                            root -> root.join(Resofom_.resolucion, JoinType.LEFT).get(Resolucion_.id)
                        )
                    );
            }
            if (criteria.getGerdisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGerdisId(), root -> root.join(Resofom_.gerdis, JoinType.LEFT).get(Gerdis_.id))
                    );
            }
        }
        return specification;
    }
}
