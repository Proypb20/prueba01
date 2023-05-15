package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Resofom;
import com.mycompany.myapp.repository.ResofomRepository;
import com.mycompany.myapp.service.dto.ResofomDTO;
import com.mycompany.myapp.service.mapper.ResofomMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resofom}.
 */
@Service
@Transactional
public class ResofomService {

    private final Logger log = LoggerFactory.getLogger(ResofomService.class);

    private final ResofomRepository resofomRepository;

    private final ResofomMapper resofomMapper;

    public ResofomService(ResofomRepository resofomRepository, ResofomMapper resofomMapper) {
        this.resofomRepository = resofomRepository;
        this.resofomMapper = resofomMapper;
    }

    /**
     * Save a resofom.
     *
     * @param resofomDTO the entity to save.
     * @return the persisted entity.
     */
    public ResofomDTO save(ResofomDTO resofomDTO) {
        log.debug("Request to save Resofom : {}", resofomDTO);
        Resofom resofom = resofomMapper.toEntity(resofomDTO);
        resofom = resofomRepository.save(resofom);
        return resofomMapper.toDto(resofom);
    }

    /**
     * Update a resofom.
     *
     * @param resofomDTO the entity to save.
     * @return the persisted entity.
     */
    public ResofomDTO update(ResofomDTO resofomDTO) {
        log.debug("Request to update Resofom : {}", resofomDTO);
        Resofom resofom = resofomMapper.toEntity(resofomDTO);
        resofom = resofomRepository.save(resofom);
        return resofomMapper.toDto(resofom);
    }

    /**
     * Partially update a resofom.
     *
     * @param resofomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResofomDTO> partialUpdate(ResofomDTO resofomDTO) {
        log.debug("Request to partially update Resofom : {}", resofomDTO);

        return resofomRepository
            .findById(resofomDTO.getId())
            .map(existingResofom -> {
                resofomMapper.partialUpdate(existingResofom, resofomDTO);

                return existingResofom;
            })
            .map(resofomRepository::save)
            .map(resofomMapper::toDto);
    }

    /**
     * Get all the resofoms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResofomDTO> findAll() {
        log.debug("Request to get all Resofoms");
        return resofomRepository.findAll().stream().map(resofomMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the resofoms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ResofomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return resofomRepository.findAllWithEagerRelationships(pageable).map(resofomMapper::toDto);
    }

    /**
     * Get one resofom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResofomDTO> findOne(Long id) {
        log.debug("Request to get Resofom : {}", id);
        return resofomRepository.findOneWithEagerRelationships(id).map(resofomMapper::toDto);
    }

    /**
     * Delete the resofom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resofom : {}", id);
        resofomRepository.deleteById(id);
    }
}
