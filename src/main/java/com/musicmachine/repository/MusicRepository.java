package com.musicmachine.repository;

import com.musicmachine.repository.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Author,Long> {

    @Query("SELECT id FROM Author")
    List<Long> getIds();
}
