package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    List<String> authors;
    int authorsListSize;
    int authorsIndex;
    Long authorOnAirId;

    AuthorRepository authorRepository;
    AlbumRepository albumRepository;
    SongRepository songRepository;

    @Autowired
    public PlayerService(AuthorRepository authorRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.authorRepository = authorRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public void initialize() {
        authors = authorRepository.getAllAuthorNames();
        authorsListSize = authors.size();
        authorsIndex = 0;
        if(authorsListSize > 0) {
            authorOnAirId = authorRepository.getIdByName(authors.get(0));
            System.out.println("Author on air id: " + authorOnAirId);
        }
    }

    public String getFirstBandName() {
        String firstBandName = "Database empty";

        if (!authors.isEmpty()) firstBandName = authors.get(authorsIndex);

        return firstBandName;
    }

    public String getFirstAlbumFromThisAuthor() {
        String firsAlbumName = "Albums empty";
        List<String> albums = albumRepository.findAlbumsByAuthorId(authorOnAirId);
        System.out.println("Number of albums: " + albums.size());
        if (!albums.isEmpty()) firsAlbumName = albums.get(0);
        return firsAlbumName;
    }

    public String getFirstSongfFromAlbum(String actualAlbumName) {
        String firstSongName = "No registered songs.";
        Long albumId = albumRepository.getAlbumIdByName(actualAlbumName);
        List<String> songs = songRepository.getSongsById(albumId);
        if (!songs.isEmpty()) firstSongName = songs.get(0);
        return firstSongName;
    }
}
