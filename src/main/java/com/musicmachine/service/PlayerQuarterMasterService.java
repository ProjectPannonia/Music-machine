package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.repository.entities.Song;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class PlayerQuarterMasterService {

    private Thread playing;
    private Player player;

    private BandRepository bandRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;
    private PlayerService playerService;

    @Autowired
    public PlayerQuarterMasterService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository, PlayerService playerService) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.playerService = playerService;
    }

//    public void play(String authorName, String albumName, String songName) {
//        Long bandId = bandRepository.getIdByName(authorName);
//        playerService.setAuthorOnAirId(bandId);
//        Long albumId = albumRepository.getAlbumIdByName(albumName);
//        playerService.setAlbumOnAirId(albumId);
//        Long songId = songRepository.getSongIDBySongName(albumId,songName);
//        refreshPlayerServiceFields(bandId, albumId);
//        System.out.println("Band id: " + bandId + ", albumId: " + albumId + ", songId: " + songId);
//        Song song = songRepository.getSongBySongId(songId);
//        System.out.println("Song path: " + song.getPathToSong());
//        playSong(song.getPathToSong());
//    }
//    private void refreshPlayerServiceFields(Long authorOnAirId, Long albumOnAirId) {
//        playerService.setAuthorOnAirId(authorOnAirId);
//        playerService.setAlbumOnAirId(albumOnAirId);
//    }
//
//    private void playSong(String pathToSong) {
//        stopSongOnAir();
//        try {
//            FileInputStream fileInputStream = new FileInputStream(pathToSong);
//            player = new Player(fileInputStream);
//            putSongOnThread(player,pathToSong);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (JavaLayerException e) {
//            e.printStackTrace();
//        }
//    }
//    private void putSongOnThread(Player player, String pathToSong) {
//        Long songID = songRepository.getSongIdByPathToSong(pathToSong);
//        Runnable runnable = () -> {
//            try {
//                player.play();
//            } catch (JavaLayerException e) {
//                e.printStackTrace();
//            }
//        };
//
//        playing = new Thread(runnable);
//        playing.start();
//
//    }
//    private void stopSongOnAir() {
//        if(player != null) {
//            player.close();
//            playing.interrupt();
//        }
//    }
//    public void stopSong() {
//        stopSongOnAir();
//    }
//    public boolean playerActive(){
//        return player.isComplete();
//    }
}
