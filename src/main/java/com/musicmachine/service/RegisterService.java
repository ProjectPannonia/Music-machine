package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.repository.entities.Album;
import com.musicmachine.repository.entities.Author;
import com.musicmachine.repository.entities.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegisterService {

    private BandRepository bandRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    private Long actualAuthorCounter;
    private Long actualAlbumCounter;
    private boolean successfullyRegistered;


    @Autowired
    public RegisterService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public DirectoryChooser getDirectory() {
        return new DirectoryChooser();
    }

    public FileChooser getFileChooser() {
        return new FileChooser();
    }

    public ObservableList getRegisteredAuthors() {
        List<String> registeredAuthorNames = bandRepository.getAllAuthorNames();
        registeredAuthorNames.forEach((n) -> System.out.println(n));
        Collections.sort(registeredAuthorNames);

        return FXCollections.observableArrayList(registeredAuthorNames);
    }

    public String registerNewBand(String newBandName, String newAlbumName, String newAlbumPath, String albumCoverFront, String albumCoverBack) {
        String responseAfterSave;
        if (bandRepository.findByName(newBandName) == null ? true : false) {
            if (checkAlbumFilesIsMp3(newAlbumPath)) {
                responseAfterSave = saveNewBand(newBandName, newAlbumName, newAlbumPath, albumCoverFront, albumCoverBack);
            } else {
                responseAfterSave = "Unsupported format! Supported formats: MP3 & WAV";
            }
        } else {
            responseAfterSave = "Band with name: " + newBandName + " is already exit!";
        }
        return responseAfterSave;
    }

    public boolean checkAlbumFilesIsMp3(String albumPath) {
        File file = new File(albumPath);
        File[] albumFiles = file.listFiles();
        List<String> filePaths = new ArrayList<>();
        for (File f : albumFiles) {
            if (isMusicFile(f.getAbsolutePath())) {
                filePaths.add(f.getAbsolutePath());
            }
        }

        return filePaths.size() > 0;
    }

    private String saveNewBand(String newBandName, String newAlbumName, String newAlbumPath, String albumCoverFront, String albumCoverBack) {
        Long createdBandId;

        bandRepository.save(new Author(newBandName));
        createdBandId = bandRepository.getIdByName(newBandName);
        createAndSaveAlbum(newAlbumName, createdBandId, newAlbumPath, albumCoverFront, albumCoverBack);

        return createdBandId != null ? "Created!" : "Problem when creating!";
    }

    private void createAndSaveAlbum(String newAlbumName, Long authorId, String albumsPath, String albumCoverFrontPath, String albumCoverBackPath) {
        Album createdAlbum;
        Long albumId;
        System.out.println("album cover front path: " + albumCoverFrontPath + ", album cover back path: " + albumCoverBackPath);

        createdAlbum = new Album(newAlbumName, authorId, albumCoverFrontPath, albumCoverBackPath);

        albumRepository.save(createdAlbum);
        albumId = albumRepository.getAlbumIdByName(newAlbumName);

        System.out.println("Created album id: " + albumId.toString());

        readMusicFiles(albumId, albumsPath);
    }


    public String saveNewAlbumForAuthor(String authorName, String newAlbumName, String newAlbumPath, String coverPathFront, String coverPathBack) {
        String saveResponse = "Invalid data";
        Long authorId = bandRepository.getIdByName(authorName);
        boolean isAlbumExist = checkAlbumNameInDb(newAlbumName);
        if (!isAlbumExist) {
            albumRepository.save(new Album(newAlbumName, authorId, coverPathFront, coverPathBack));
            Long savedAlbumId = albumRepository.getAlbumIdByName(newAlbumName);
            readMusicFiles(savedAlbumId, newAlbumPath);
            saveResponse = newAlbumName + " saved.";
            successfullyRegistered = true;
        } else {
            successfullyRegistered = false;
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
        successfullyRegistered = true;
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

        if (format.equals("mp3")) {
            itIsMusicFile = true;
        } else if (format.equals("wav")) {
            itIsMusicFile = true;
        }
        System.out.println("Music file: " + itIsMusicFile);
        return itIsMusicFile;
    }

    public boolean isSuccessfullyRegistered() {
        return successfullyRegistered;
    }

    public void setSuccessfullyRegistered(boolean successfullyRegistered) {
        this.successfullyRegistered = successfullyRegistered;
    }
}
