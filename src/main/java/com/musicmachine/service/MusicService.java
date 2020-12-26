package com.musicmachine.service;

import com.musicmachine.repository.MusicRepository;
import com.musicmachine.repository.entities.Author;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private MusicRepository musicRepository;

    @Autowired
    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    private ArrayList<String> authors = new ArrayList<>();
    private ArrayList<String> albums = new ArrayList<>();

    private int actualAuthorCounter;
    private int actualAlbumCounter;

    public void initializeServie() {
        authors.add("Sabaton");
        authors.add("Manowar");
        authors.add("Epica");

        actualAuthorCounter = 0;

        if(authors.size() != 0) {

        }
    }
    public ObservableList<String> getAllBands() {
        //musicRepository.getAllBands();
        musicRepository.findAll();
        return null;
    }

    public void exit() {
        Platform.exit();
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<String> albums) {
        this.albums = albums;
    }

    public int getActualAuthorCounter() {
        return actualAuthorCounter;
    }

    public void setActualAuthorCounter(int actualAuthorCounter) {
        this.actualAuthorCounter = actualAuthorCounter;
    }

    public int getActualAlbumCounter() {
        return actualAlbumCounter;
    }

    public void setActualAlbumCounter(int actualAlbumCounter) {
        this.actualAlbumCounter = actualAlbumCounter;
    }


    public DirectoryChooser getDirectory() {
        return new DirectoryChooser();
    }

    public ObservableList getRegisteredAuthors() {
        List<Author> authors = musicRepository.findAll();
        List<String> authorsName = new ArrayList<>();
        authors.forEach((n) -> authorsName.add(n.getAuthorName()));
        Collections.sort(authorsName);
        return FXCollections.observableArrayList(authorsName);
    }
}
