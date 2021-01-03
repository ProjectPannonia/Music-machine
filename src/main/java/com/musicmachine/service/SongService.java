package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
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

    private BandRepository bandRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    @Autowired
    public SongService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository){
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public ObservableList<String> getSongsByAlbumName(String nextAuthor) {
        actualAuthorId = bandRepository.getIdByName(nextAuthor);
        //List<String> songList =
        return null;
    }

    public ObservableList<String> listAlbumNames(String nextAuthor) {
        actualAuthorId = bandRepository.getIdByName(nextAuthor);
        List<String> albumNames = albumRepository.findAlbumsByAuthorId(actualAuthorId);
        Collections.sort(albumNames);

        return FXCollections.observableArrayList(albumNames);
    }
}
