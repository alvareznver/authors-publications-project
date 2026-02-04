package com.editorial.publications.domain.repository;

import com.editorial.publications.domain.entity.Publication;
import com.editorial.publications.domain.entity.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("SELECT p FROM Publication p WHERE p.isActive = true")
    Page<Publication> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Publication p WHERE p.authorId = :authorId AND p.isActive = true")
    Page<Publication> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

    @Query("SELECT p FROM Publication p WHERE p.status = :status AND p.isActive = true")
    Page<Publication> findByStatus(@Param("status") PublicationStatus status, Pageable pageable);

    @Query("SELECT p FROM Publication p WHERE p.isActive = true AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Publication> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Publication p WHERE p.authorId = :authorId AND p.status = :status AND p.isActive = true")
    List<Publication> findByAuthorIdAndStatus(@Param("authorId") Long authorId, @Param("status") PublicationStatus status);

    @Query("SELECT COUNT(p) FROM Publication p WHERE p.authorId = :authorId AND p.isActive = true")
    long countByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT COUNT(p) FROM Publication p WHERE p.status = :status AND p.isActive = true")
    long countByStatus(@Param("status") PublicationStatus status);

    @Query("SELECT COUNT(p) FROM Publication p WHERE p.isActive = true")
    long countActive();
}
