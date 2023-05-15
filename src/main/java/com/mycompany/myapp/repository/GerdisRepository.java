package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Gerdis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gerdis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GerdisRepository extends JpaRepository<Gerdis, Long>, JpaSpecificationExecutor<Gerdis> {}
