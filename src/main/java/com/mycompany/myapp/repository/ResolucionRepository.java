package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resolucion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Resolucion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResolucionRepository extends JpaRepository<Resolucion, Long>, JpaSpecificationExecutor<Resolucion> {}
