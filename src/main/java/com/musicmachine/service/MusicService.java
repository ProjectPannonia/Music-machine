package com.musicmachine.service;

import com.musicmachine.repository.MusicRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private MusicRepository musicRepository;
    private Long actualAuthorCounter;
    private Long actualAlbumCounter;
    private List<Long> authorIds;
    private int authorIdsSize;
    private int actualAuthorsIndex;

    @Autowired
    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public void initialize() {
        authorIds = musicRepository.getIds();
        Collections.sort(authorIds);

        actualAuthorCounter = authorIds.get(0);
        authorIdsSize = authorIds.size();
        actualAuthorsIndex = 0;
    }

    public DirectoryChooser getDirectory() {
        return new DirectoryChooser();
    }

    public ObservableList getRegisteredAuthors() {
          List<String> registeredAuthorNames = musicRepository.getAllAuthorNames();
//        Collections.sort(registeredAuthorNames);
//        return FXCollections.observableArrayList(registeredAuthorNames);
        Collections.sort(registeredAuthorNames);
        return  FXCollections.observableArrayList(registeredAuthorNames);
    }

    public String getFirstBandName() {
        return musicRepository.getOne(actualAuthorCounter).getAuthorName();
    }

    public String giveNextAuthor() {
        actualAuthorsIndex++;

        if(actualAuthorsIndex >= authorIdsSize) actualAuthorsIndex--;

        return musicRepository.getOne(new Long(actualAuthorsIndex)).getAuthorName();
    }
    public String givePreviousAuthor() {
        actualAuthorsIndex--;

        if(actualAuthorsIndex < 0) actualAuthorsIndex++;

        return musicRepository.getOne(new Long(actualAuthorsIndex)).getAuthorName();
    }
    public void exit() {
        Platform.exit();
    }

    public Long getActualAuthorCounter() {
        return actualAuthorCounter;
    }

    public void setActualAuthorCounter(Long actualAuthorCounter) {
        this.actualAuthorCounter = actualAuthorCounter;
    }

    public Long getActualAlbumCounter() {
        return actualAlbumCounter;
    }

    public void setActualAlbumCounter(Long actualAlbumCounter) {
        this.actualAlbumCounter = actualAlbumCounter;
    }


}
