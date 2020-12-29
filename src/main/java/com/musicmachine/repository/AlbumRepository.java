package com.musicmachine.repository;

import com.musicmachine.repository.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    //"UPDATE UnencryptedLoginData l SET l.userId = : userId WHERE l.domain = : domainName")
    @Query(value = "insert into Album (albumName,author_Id) values(:newAlbumName, :authorId)", nativeQuery = true)
    void createAlbumForSpecifiedId(@Param("newAlbumName") String newAlbumName, @Param("authorId") Long authorId);

    @Query(value = "select s.id from Album s where s.albumName = :newAlbumName")
    Long getAlbumIdByName(@Param("newAlbumName") String newAlbumName);
}
