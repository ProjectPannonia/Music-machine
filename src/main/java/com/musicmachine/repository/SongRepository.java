package com.musicmachine.repository;

import com.musicmachine.repository.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    //(value = "insert into Album (albumName,authorId) values(:newAlbumName, :authorId)")
    @Query(value = "insert into Song (pathToSong, songName, albumId) values(:absolutePath, :name, :albumId)", nativeQuery = true)
    void saveWithAlbumId(@Param("name") String name, @Param("absolutePath") String absolutePath, @Param("albumId") Long albumId);
}
