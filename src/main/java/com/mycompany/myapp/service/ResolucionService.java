package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Resolucion;
import com.mycompany.myapp.repository.ResolucionRepository;
import com.mycompany.myapp.service.dto.ResolucionDTO;
import com.mycompany.myapp.service.mapper.ResolucionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resolucion}.
 */
@Service
@Transactional
public class ResolucionService {

    private final Logger log = LoggerFactory.getLogger(ResolucionService.class);

    private final ResolucionRepository resolucionRepository;

    private final ResolucionMapper resolucionMapper;

    public ResolucionService(ResolucionRepository resolucionRepository, ResolucionMapper resolucionMapper) {
        this.resolucionRepository = resolucionRepository;
        this.resolucionMapper = resolucionMapper;
    }

    /**
     * Save a resolucion.
     *
     * @param resolucionDTO the entity to save.
     * @return the persisted entity.
     */
    public ResolucionDTO save(ResolucionDTO resolucionDTO) {
        log.debug("Request to save Resolucion : {}", resolucionDTO);
        Resolucion resolucion = resolucionMapper.toEntity(resolucionDTO);
        resolucion = resolucionRepository.save(resolucion);
        return resolucionMapper.toDto(resolucion);
    }

    /**
     * Update a resolucion.
     *
     * @param resolucionDTO the entity to save.
     * @return the persisted entity.
     */
    public ResolucionDTO update(ResolucionDTO resolucionDTO) {
        log.debug("Request to update Resolucion : {}", resolucionDTO);
        Resolucion resolucion = resolucionMapper.toEntity(resolucionDTO);
        resolucion = resolucionRepository.save(resolucion);
        return resolucionMapper.toDto(resolucion);
    }

    /**
     * Partially update a resolucion.
     *
     * @param resolucionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResolucionDTO> partialUpdate(ResolucionDTO resolucionDTO) {
        log.debug("Request to partially update Resolucion : {}", resolucionDTO);

        return resolucionRepository
            .findById(resolucionDTO.getId())
            .map(existingResolucion -> {
                resolucionMapper.partialUpdate(existingResolucion, resolucionDTO);

                return existingResolucion;
            })
            .map(resolucionRepository::save)
            .map(resolucionMapper::toDto);
    }

    /**
     * Get all the resolucions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResolucionDTO> findAll() {
        log.debug("Request to get all Resolucions");
        return resolucionRepository.findAll().stream().map(resolucionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one resolucion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResolucionDTO> findOne(Long id) {
        log.debug("Request to get Resolucion : {}", id);
        return resolucionRepository.findById(id).map(resolucionMapper::toDto);
    }

    /**
     * Delete the resolucion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resolucion : {}", id);
        resolucionRepository.deleteById(id);
    }
}
