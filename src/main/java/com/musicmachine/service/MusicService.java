package com.musicmachine.service;

import javafx.application.Platform;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MusicService {

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


}
