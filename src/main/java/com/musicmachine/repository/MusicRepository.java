package com.musicmachine.repository;

import com.musicmachine.repository.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Author,Long> {

    @Query("SELECT id FROM Author")
    List<Long> getIds();

    @Query("select authorName from Author")
    List<String> getAllAuthorNames();

    @Query(value = "select s from Author s where s.authorName = :authorName")
    Author getByBandName(@Param("authorName") String authorName);

    @Query(value = "select s from Author s where s.authorName = :authorName")
    Long getIdByName(@Param("authorName") String authorName);

    @Query(value = "select s from Author s where s.id = :authorId")
    Author getAuthorById(@Param("authorId") Long authorId);
}
