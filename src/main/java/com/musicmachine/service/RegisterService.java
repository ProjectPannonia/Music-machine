package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
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
import java.util.Collections;
import java.util.List;

@Service
public class RegisterService {

    private AuthorRepository authorRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    private Long actualAuthorCounter;
    private Long actualAlbumCounter;



    private int actualAuthorsIndex;

    @Autowired
    public RegisterService(AuthorRepository authorRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.authorRepository = authorRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    private List<String> registeredAuthors;
    private int registeredAuthorsSize;
    private int authorOnAirIndex;
    public void initialize() {
        registeredAuthors = authorRepository.getAllAuthorNames();
        registeredAuthorsSize = registeredAuthors.size();
        authorOnAirIndex = 0;
    }
    public String getFirstBandName() {
        String firstBandName = "Database empty";

        if (authorRepository.count() > 0) firstBandName = registeredAuthors.get(0);

        return firstBandName;
    }
    public String getFirstAlbumFromThisAuthor(String actualAuthorName) {
        Long authorId = authorRepository.getIdByName(actualAuthorName);
        List<String> albumNamesForThisAuthor = albumRepository.findAlbumsByAuthorId(authorId);
        System.out.println(albumNamesForThisAuthor.size());
        return albumNamesForThisAuthor.get(0);
    }
    public String getFirstSongfFromAlbum(String actualAlbumName) {
        Long albumId = albumRepository.getAlbumIdByName(actualAlbumName);
        List<String> songs = songRepository.getSongsById(albumId);
        return songs.get(0);
    }
    public DirectoryChooser getDirectory() {
        return new DirectoryChooser();
    }

    public ObservableList getRegisteredAuthors() {
        List<String> registeredAuthorNames = authorRepository.getAllAuthorNames();
        registeredAuthorNames.forEach((n) -> System.out.println(n));
        Collections.sort(registeredAuthorNames);

        return FXCollections.observableArrayList(registeredAuthorNames);
    }



    public String giveNextAuthor() {
        String nextAuthorName;
        authorOnAirIndex++;
        if(authorOnAirIndex < registeredAuthorsSize){
            nextAuthorName = registeredAuthors.get(authorOnAirIndex);
        }else {
            authorOnAirIndex--;
            nextAuthorName = registeredAuthors.get(authorOnAirIndex);
        }
        return nextAuthorName;
    }

    public String givePreviousAuthor() {
        actualAuthorsIndex--;

        if (actualAuthorsIndex < 1) actualAuthorsIndex++;

        return authorRepository.getOne(new Long(actualAuthorsIndex)).getAuthorName();
    }

    public String saveNewAuthor(String newAuthorName, String newAlbumName, String newAlbumPath) {
        String response;
        Author searchAuthorInDb = authorRepository.findByName(newAuthorName);
        if (searchAuthorInDb == null) {
            System.out.println("New author");
            authorRepository.save(new Author(newAuthorName));
            Long createdId = authorRepository.getIdByName(newAuthorName);
            createAlbum(newAlbumName, createdId, newAlbumPath);

            System.out.println("Created id: " + createdId.toString());
            response = "New author saved";
        } else {
            response = "This author already exist!";
        }
        return response;
    }

    private void createAlbum(String newAlbumName, Long authorId, String albumsPath) {
        Album createdAlbum = new Album(newAlbumName, authorId);
        albumRepository.save(createdAlbum);
        Long albumId = albumRepository.getAlbumIdByName(newAlbumName);
        System.out.println("Created album id: " + albumId.toString());
        readMusicFiles(albumId, albumsPath);
    }


    public String saveNewAlbumForAuthor(String authorName, String newAlbumName, String newAlbumPath) {
        String saveResponse = "Invalid data";
        Long authorId = authorRepository.getIdByName(authorName);
        boolean isAlbumExist = checkAlbumNameInDb(newAlbumName);
        if(!isAlbumExist){
            albumRepository.save(new Album(newAlbumName,authorId));
            Long savedAlbumId = albumRepository.getAlbumIdByName(newAlbumName);
            readMusicFiles(savedAlbumId, newAlbumPath);
            saveResponse = newAlbumName + " saved.";
        }
        return saveResponse;
    }
    private void readMusicFiles(Long albumId, String albumsPath) {
        File[] filesInFolder = new File(albumsPath).listFiles();
        for (File file : filesInFolder) {
            if (!file.isDirectory() && isMusicFile(file.getName())) {
                System.out.println(file.getName() + ", " + file.getAbsolutePath());
                Song song = new Song(file.getName(), file.getAbsolutePath(), albumId);
                songRepository.save(song);
            }
        }
    }
    private boolean checkAlbumNameInDb(String newAlbumName) {
        Album album = albumRepository.getAlbumByName(newAlbumName);

        return album != null ? true : false;
    }

    private boolean isMusicFile(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        System.out.println("Last dot index: " + lastDotIndex);
        String format = fileName.substring(lastDotIndex + 1).toLowerCase();
        System.out.println("Format: " + format);
        boolean itIsMusicFile = false;

        if(format.equals("mp3")){
            itIsMusicFile = true;
        }else if(format.equals("wav")){
            itIsMusicFile = true;
        }
        System.out.println("Music file: " + itIsMusicFile);
        return itIsMusicFile;
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