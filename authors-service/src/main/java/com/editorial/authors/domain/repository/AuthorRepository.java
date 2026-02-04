package com.editorial.authors.domain.repository;

import com.editorial.authors.domain.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByEmail(String email);

    @Query("SELECT a FROM Author a WHERE a.isActive = true")
    Page<Author> findAllActive(Pageable pageable);

    @Query("SELECT a FROM Author a WHERE a.isActive = true AND LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Author> searchByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT a FROM Author a WHERE a.isActive = true AND a.id IN :ids")
    List<Author> findAllByIds(@Param("ids") List<Long> ids);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT COUNT(a) FROM Author a WHERE a.isActive = true")
    long countActive();
}
