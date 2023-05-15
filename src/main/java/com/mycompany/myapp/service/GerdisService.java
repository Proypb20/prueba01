package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Gerdis;
import com.mycompany.myapp.repository.GerdisRepository;
import com.mycompany.myapp.service.dto.GerdisDTO;
import com.mycompany.myapp.service.mapper.GerdisMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gerdis}.
 */
@Service
@Transactional
public class GerdisService {

    private final Logger log = LoggerFactory.getLogger(GerdisService.class);

    private final GerdisRepository gerdisRepository;

    private final GerdisMapper gerdisMapper;

    public GerdisService(GerdisRepository gerdisRepository, GerdisMapper gerdisMapper) {
        this.gerdisRepository = gerdisRepository;
        this.gerdisMapper = gerdisMapper;
    }

    /**
     * Save a gerdis.
     *
     * @param gerdisDTO the entity to save.
     * @return the persisted entity.
     */
    public GerdisDTO save(GerdisDTO gerdisDTO) {
        log.debug("Request to save Gerdis : {}", gerdisDTO);
        Gerdis gerdis = gerdisMapper.toEntity(gerdisDTO);
        gerdis = gerdisRepository.save(gerdis);
        return gerdisMapper.toDto(gerdis);
    }

    /**
     * Update a gerdis.
     *
     * @param gerdisDTO the entity to save.
     * @return the persisted entity.
     */
    public GerdisDTO update(GerdisDTO gerdisDTO) {
        log.debug("Request to update Gerdis : {}", gerdisDTO);
        Gerdis gerdis = gerdisMapper.toEntity(gerdisDTO);
        gerdis = gerdisRepository.save(gerdis);
        return gerdisMapper.toDto(gerdis);
    }

    /**
     * Partially update a gerdis.
     *
     * @param gerdisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GerdisDTO> partialUpdate(GerdisDTO gerdisDTO) {
        log.debug("Request to partially update Gerdis : {}", gerdisDTO);

        return gerdisRepository
            .findById(gerdisDTO.getId())
            .map(existingGerdis -> {
                gerdisMapper.partialUpdate(existingGerdis, gerdisDTO);

                return existingGerdis;
            })
            .map(gerdisRepository::save)
            .map(gerdisMapper::toDto);
    }

    /**
     * Get all the gerdis.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GerdisDTO> findAll() {
        log.debug("Request to get all Gerdis");
        return gerdisRepository.findAll().stream().map(gerdisMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one gerdis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GerdisDTO> findOne(Long id) {
        log.debug("Request to get Gerdis : {}", id);
        return gerdisRepository.findById(id).map(gerdisMapper::toDto);
    }

    /**
     * Delete the gerdis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Gerdis : {}", id);
        gerdisRepository.deleteById(id);
    }
}
