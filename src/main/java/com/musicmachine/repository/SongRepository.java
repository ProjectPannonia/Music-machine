package com.musicmachine.repository;

import com.musicmachine.repository.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query(value = "insert into Song (pathToSong, songName, albumId) values(:absolutePath, :name, :albumId)", nativeQuery = true)
    void saveWithAlbumId(@Param("name") String name, @Param("absolutePath") String absolutePath, @Param("albumId") Long albumId);

    @Query(value = "select s.songName from Song s where s.albumId = :albumId")
    List<String> getSongsByAlbumId(@Param("albumId") Long albumId);

    @Query(value = "select s.id from Song s where s.albumId = :albumId and s.songName = :songName")
    Long getSongIDBySongName(@Param("albumId") Long albumId, @Param("songName") String songName);

    @Query(value = "select s from Song s where s.id= :songId")
    Song getSongBySongId(Long songId);

    @Query(value = "select s.id from Song s where s.pathToSong = :pathToSong")
    Long getSongIdByPathToSong(@Param("pathToSong") String pathToSong);
}
