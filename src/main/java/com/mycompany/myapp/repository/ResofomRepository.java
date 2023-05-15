package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resofom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Resofom entity.
 */
@Repository
public interface ResofomRepository extends JpaRepository<Resofom, Long>, JpaSpecificationExecutor<Resofom> {
    default Optional<Resofom> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Resofom> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Resofom> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct resofom from Resofom resofom left join fetch resofom.resolucion left join fetch resofom.gerdis",
        countQuery = "select count(distinct resofom) from Resofom resofom"
    )
    Page<Resofom> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct resofom from Resofom resofom left join fetch resofom.resolucion left join fetch resofom.gerdis")
    List<Resofom> findAllWithToOneRelationships();

    @Query("select resofom from Resofom resofom left join fetch resofom.resolucion left join fetch resofom.gerdis where resofom.id =:id")
    Optional<Resofom> findOneWithToOneRelationships(@Param("id") Long id);
}
