package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SongService {
    private Long actualAuthorId;
    private Long actualAlbumId;

    private AuthorRepository authorRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    @Autowired
    public SongService(AuthorRepository authorRepository, AlbumRepository albumRepository, SongRepository songRepository){
        this.authorRepository = authorRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public ObservableList<String> getSongsByAlbumName(String nextAuthor) {
        actualAuthorId = authorRepository.getIdByName(nextAuthor);
        //List<String> songList =
        return null;
    }

    public ObservableList<String> listAlbumNames(String nextAuthor) {
        actualAuthorId = authorRepository.getIdByName(nextAuthor);
        List<String> albumNames = albumRepository.findAlbumsByAuthorId(actualAuthorId);
        Collections.sort(albumNames);

        return FXCollections.observableArrayList(albumNames);
    }
}
