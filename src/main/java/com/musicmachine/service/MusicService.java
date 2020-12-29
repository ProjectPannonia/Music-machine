package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.MusicRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private MusicRepository musicRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    private Long actualAuthorCounter;
    private Long actualAlbumCounter;
    private List<Long> authorIds;
    private int authorIdsSize;
    private int actualAuthorsIndex;

    @Autowired
    public MusicService(MusicRepository musicRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.musicRepository = musicRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }


    public void initialize() {
        authorIds = musicRepository.getIds();
        Collections.sort(authorIds);

        if(!authorIds.isEmpty()) {
            actualAuthorCounter = authorIds.get(0);
        }
        authorIdsSize = authorIds.size();
        actualAuthorsIndex = 0;
    }

    public DirectoryChooser getDirectory() {
        return new DirectoryChooser();
    }

    public ObservableList getRegisteredAuthors() {
        List<String> registeredAuthorNames = musicRepository.getAllAuthorNames();
        Collections.sort(registeredAuthorNames);

        return  FXCollections.observableArrayList(registeredAuthorNames);
    }

    public String getFirstBandName() {
        Long countBandsInDb = musicRepository.count();
        String firstBandName = "Database empty";

        if(countBandsInDb > 0) {
            firstBandName = musicRepository.getOne(actualAuthorCounter).getAuthorName();
        }
        return firstBandName;
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
        String response;
        Author searchAuthorInDb = musicRepository.findByName(newAuthorName);
        if (searchAuthorInDb == null) {
            System.out.println("New author");
            musicRepository.save(new Author(newAuthorName));
            Long createdId = musicRepository.getIdByName(newAuthorName);
            createAlbum(newAlbumName,createdId,newAlbumPath);

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

    private void readMusicFiles(Long albumId, String albumsPath) {
        File[] filesInFolder = new File(albumsPath).listFiles();
        for (File file : filesInFolder) {
            if (!file.isDirectory()) {
                System.out.println(file.getName() + ", " + file.getAbsolutePath());
                Song song = new Song(file.getName(), file.getAbsolutePath(), albumId);
                songRepository.save(song);
            }
        }
        //saveSonsToDb(filesInFolder, albumId);
    }

    private void saveSonsToDb(File[] filesInFolder, Long albumId) {
        List<Song> songs = new ArrayList<>();
        for (File file : filesInFolder) {
            songRepository.saveWithAlbumId(file.getName(),file.getAbsolutePath(), albumId);
        }
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
