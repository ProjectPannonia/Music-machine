package com.musicmachine.service;

import com.musicmachine.repository.MusicRepository;
import com.musicmachine.repository.entities.Album;
import com.musicmachine.repository.entities.Author;
import com.musicmachine.repository.entities.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
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
    public String saveNewAuthor(String newAuthorName, String newAlbumName, String newAlbumPath) {
        System.out.println("New author name: " + newAuthorName + ", new album name: " + newAlbumName + ", new album path: " + newAlbumPath);
        boolean invalidNewAuthorName = newAuthorName != null && !newAuthorName.equals("");
        boolean invalidNewAlbumName = newAlbumName != null && !newAlbumName.equals("");
        boolean invalidPath = newAlbumPath != null && !newAlbumPath.equals("");
        String saveResponse = "Invalid data";

        if (invalidNewAuthorName && invalidNewAlbumName && invalidPath) {
            Author authorFromDb = musicRepository.getByBandName(newAuthorName);

            if(authorFromDb == null) {
                Author newAuthorToDb = new Author(newAuthorName);
                saveResponse = "New author: " + newAuthorName + " saved.";
            }
        }
        return saveResponse;
    }
    public String saveNewAlbumForAuthor(String authorName, String newAlbumName, String newAlbumPath) {
        String saveResponse = "Invalid data";
        Long authorId = musicRepository.getIdByName(authorName);
        //saveAlbumForAuthor(authorId, newAlbumName, newAlbumPath);

        return saveResponse;
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
