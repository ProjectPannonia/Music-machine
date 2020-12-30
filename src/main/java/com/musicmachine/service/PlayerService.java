package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.repository.entities.Song;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {

    private List<String> authorNamesList;
    private int authorsListSize;
    private int authorNamesListIndex;
    private Long authorOnAirId;

    private List<String> albumNamesList;
    private int albumListSize;
    private int albumNamesListIndex;
    private Long albumOnAirId;

    private List<String> songNamesList;
    private int songListSize;
    private int songNamesListIndex;



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
        authorNamesList = authorRepository.getAllAuthorNames();
        authorsListSize = authorNamesList.size();
        authorNamesListIndex = 0;
        if(authorsListSize > 0) {
            authorOnAirId = authorRepository.getIdByName(authorNamesList.get(0));
            albumNamesList = albumRepository.findAlbumsByAuthorId(authorOnAirId);
            albumListSize = albumNamesList.size();
            if(albumListSize > 0){
                albumNamesListIndex = 0;
                albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));

                songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
                System.out.println("Song list names size: " + songNamesList.size());
                songListSize = songNamesList.size();
                if(songListSize > 0) {
                    songNamesListIndex = 0;
                }
            }
            Collections.sort(albumNamesList);
        }
    }

    public String getFirstBandName() {
        String firstBandName = "Database empty";

        if (!authorNamesList.isEmpty()) firstBandName = authorNamesList.get(authorNamesListIndex);

        return firstBandName;
    }

    public String getFirstAlbumFromThisAuthor() {
        String firsAlbumName = "Albums empty";
        if (!albumNamesList.isEmpty()) firsAlbumName = albumNamesList.get(albumNamesListIndex);
        return firsAlbumName;
    }

    public String getFirstSongfFromAlbum(String actualAlbumName) {
        String firstSongName = "No registered songs.";
        Long albumId = albumRepository.getAlbumIdByName(actualAlbumName);
        songNamesList = songRepository.getSongsByAlbumId(albumId);

        if (!songNamesList.isEmpty()) firstSongName = songNamesList.get(songNamesListIndex);
        return firstSongName;
    }



    public String giveNextBandName() {
        if(authorNamesListIndex + 1 < authorsListSize) {
            authorNamesListIndex++;
            refreshAlbumNameListAndSongNamesList();
        }
        return authorNamesList.get(authorNamesListIndex);
    }
    public String givePreviousBandName() {
        if(authorNamesListIndex - 1 >= 0) {
            authorNamesListIndex--;
            refreshAlbumNameListAndSongNamesList();
        }
        return authorNamesList.get(authorNamesListIndex);
    }
    private void refreshAlbumNameListAndSongNamesList() {
        authorOnAirId = authorRepository.getIdByName(authorNamesList.get(authorNamesListIndex));
        // Refresh albums list
        albumNamesList = albumRepository.findAlbumsByAuthorId(authorOnAirId);
        albumListSize = albumNamesList.size();

        // Set albumNamesListIndex to 0
        albumNamesListIndex = 0;
        // Set albumOnAirId to the first album's albumId
        albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));
        // Refresh songs by albumId
        songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
        songListSize = songNamesList.size();
        songNamesListIndex = 0;
    }
    public String giveNextAlbum() {
        if (albumNamesListIndex + 1 < albumListSize) {
            albumNamesListIndex++;
            refreshSongNamesList();
        }
        return albumNamesList.get(albumNamesListIndex);
    }
    public String givePreviousAlbum() {
        if(albumNamesListIndex -1 >= 0) {
            albumNamesListIndex--;
            refreshSongNamesList();
        }
        return albumNamesList.get(albumNamesListIndex);
    }
    private void refreshSongNamesList() {
        // Set albumOnAirId to the next album's albumId
        albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));
        // Refresh songs by albumId
        songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
        songListSize = songNamesList.size();
        songNamesListIndex = 0;
    }
    public String getBandFirstAlbum() {
        return albumNamesList.get(albumNamesListIndex);
    }
    public String getFirstSongNameFromAlbum() {
        return songNamesList.get(songNamesListIndex);
    }

    public String giveNextSong() {
        if(songNamesListIndex + 1 < songListSize) songNamesListIndex++;
        return songNamesList.get(songNamesListIndex);
    }

    public String givePreviousSong() {
        if(songNamesListIndex - 1 >= 0) songNamesListIndex--;
        return songNamesList.get(songNamesListIndex);
    }

    private void playWaw(String pathToSong) {
        Long currentFrame;
        Clip clip;
        AudioInputStream audioInputStream;
        String status;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(pathToSong).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAuthorNamesList() {
        return authorNamesList;
    }

    public int getAuthorsListSize() {
        return authorsListSize;
    }

    public int getAuthorNamesListIndex() {
        return authorNamesListIndex;
    }

    public Long getAuthorOnAirId() {
        return authorOnAirId;
    }

    public List<String> getAlbumNamesList() {
        return albumNamesList;
    }

    public int getAlbumListSize() {
        return albumListSize;
    }

    public int getAlbumNamesListIndex() {
        return albumNamesListIndex;
    }

    public Long getAlbumOnAirId() {
        return albumOnAirId;
    }

    public List<String> getSongNamesList() {
        return songNamesList;
    }

    public int getSongListSize() {
        return songListSize;
    }

    public int getSongNamesListIndex() {
        return songNamesListIndex;
    }
}
