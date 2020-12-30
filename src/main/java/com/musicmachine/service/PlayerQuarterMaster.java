package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.repository.entities.Song;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class PlayerQuarterMaster {

    private Thread playing;
    private Player player;

    private AuthorRepository authorRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    private PlayerService playerService;

    @Autowired
    public PlayerQuarterMaster(AuthorRepository authorRepository, AlbumRepository albumRepository, SongRepository songRepository, PlayerService playerService) {
        this.authorRepository = authorRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.playerService = playerService;
    }

    public void play(String authorName, String albumName, String songName) {
        Long bandId = authorRepository.getIdByName(authorName);
        Long albumId = albumRepository.getAlbumIdByName(albumName);
        Long songId = songRepository.getSongIDBySongName(albumId,songName);
        System.out.println("Band id: " + bandId + ", albumId: " + albumId + ", songId: " + songId);
        Song song = songRepository.getSongBySongId(songId);
        System.out.println("Song path: " + song.getPathToSong());
        playSong(song.getPathToSong());
    }

    private void playSong(String pathToSong) {
        stopSongOnAir();
        try {
            FileInputStream fileInputStream = new FileInputStream(pathToSong);
            player = new Player(fileInputStream);
            putSongOnThread(player);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
    private void putSongOnThread(Player player) {
        Runnable runnable = () -> {
            try {
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        };

        playing = new Thread(runnable);
        playing.start();
    }
    private void stopSongOnAir() {
        if(player != null) {
            player.close();
            playing.interrupt();
        }
    }
    public void stopSong() {
        stopSongOnAir();
    }
    public boolean playerActive(){
        return player.isComplete();
    }
}
