package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.repository.entities.Song;
import com.musicmachine.service.onair.OnAirData;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private OnAirData onAirData;
    private Player player;
    private Thread testThread;
    private Thread playerThread;

    private BandRepository bandRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    @Autowired
    public PlayerService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public void initialize() {
        onAirData = new OnAirData();
        onAirData.setRegisteredBands(bandRepository.getAllAuthorNames());
        if (onAirData.getRegisteredBandsSize() > 0) {
            // Set the authorOnAirId
            Long newBandOnAirId = bandRepository.getIdByName(onAirData.giveFirstElement("author"));
            onAirData.setBandOnAirId(newBandOnAirId);
            // Set albumNamesList by auhorOnAirId
            onAirData.setActualBandAlbums(albumRepository.findAlbumsByAuthorId(onAirData.getBandOnAirId()));
            // If albumNamesList not empty ->
            if (!onAirData.getActualBandAlbums().isEmpty()) {
                // -> set albumOnAirId
                onAirData.setAlbumOnAirId(albumRepository.getAlbumIdByName(onAirData.giveFirstElement("album")));
                onAirData.setActualAlbumTrackList(songRepository.getSongsByAlbumId(onAirData.getAlbumOnAirId()));
                if (!onAirData.getActualAlbumTrackList().isEmpty())
                    onAirData.setActualAlbumTrackListIndex(0);
            }
        }
    }

    public String nextBand() {
        String nextBand = onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
        ;
        if (onAirData.getRegisteredBandsIndex() + 1 < onAirData.getRegisteredBandsSize()) {
            nextBand = onAirData.getNextBandName();
            Long nextBandOnAirId = bandRepository.getIdByName(nextBand);
            onAirData.setBandOnAirId(nextBandOnAirId);
            refreshBandAlbumList(nextBandOnAirId);
        }
        return nextBand;
    }

    public String previousBand() {
        String previousBand = onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
        if (onAirData.getRegisteredBandsIndex() - 1 >= 0) {
            previousBand = onAirData.getPreviousBandName();
            Long previousBandOnAirId = bandRepository.getIdByName(previousBand);
            onAirData.setBandOnAirId(previousBandOnAirId);
            refreshBandAlbumList(previousBandOnAirId);
        }
        return previousBand;
    }

    private void refreshBandAlbumList(Long bandId) {
        List<String> bandAlbums = albumRepository.findAlbumsByAuthorId(bandId);
        onAirData.setActualBandAlbums(bandAlbums);
        onAirData.setActualBandAlbumsSize(bandAlbums.size());
        onAirData.setActualBandAlbumsIndex(0);
        Long firstAlbumId = albumRepository.getAlbumIdByName(bandAlbums.get(0));
        refreshAlbumSongList(firstAlbumId);
    }

    private void refreshAlbumSongList(Long firstAlbumId) {
        List<String> songsOnAlbum = songRepository.getSongsByAlbumId(firstAlbumId);
        onAirData.setActualAlbumTrackList(songsOnAlbum);
        onAirData.setActualAlbumTrackListSize(songsOnAlbum.size());
        onAirData.setActualAlbumTrackListIndex(0);
    }


    public String giveNextAlbum() {
        String nextAlbum = onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex());
        if (onAirData.getActualBandAlbumsIndex() + 1 < onAirData.getActualBandAlbumsSize()) {
            nextAlbum = onAirData.getNextAlbumName();
            Long nextAlbumOnAirId = albumRepository.getAlbumIdByName(nextAlbum);
            onAirData.setAlbumOnAirId(nextAlbumOnAirId);
            refreshSongList(nextAlbumOnAirId);
        }
        return nextAlbum;
    }

    public String givePreviousAlbum() {
        String previousAlbum = onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex());
        if (onAirData.getActualBandAlbumsIndex() - 1 >= 0) {
            previousAlbum = onAirData.getPreviousAlbumName();
            Long previousAlbumOnAirId = albumRepository.getAlbumIdByName(previousAlbum);
            onAirData.setAlbumOnAirId(previousAlbumOnAirId);
            refreshSongList(previousAlbumOnAirId);
        }
        return previousAlbum;
    }

    private void refreshSongList(Long nextAlbumOnAirId) {
        List<String> songsOnAlbum = songRepository.getSongsByAlbumId(nextAlbumOnAirId);
        onAirData.setActualAlbumTrackList(songsOnAlbum);
        onAirData.setActualAlbumTrackListSize(songsOnAlbum.size());
        onAirData.setActualAlbumTrackListIndex(0);
    }


    public String giveNextSong() {
        String nextSong = onAirData.getActualAlbumTrackList().get(onAirData.getActualAlbumTrackListIndex());

        if (onAirData.getActualAlbumTrackListIndex() + 1 < onAirData.getActualAlbumTrackListSize())
            nextSong = onAirData.getNextSongName();

        return nextSong;
    }

    public String givePreviousSong() {
        String previousSong = onAirData.getActualAlbumTrackList().get(onAirData.getActualAlbumTrackListIndex());
        if (onAirData.getActualAlbumTrackListIndex() - 1 >= 0)
            previousSong = onAirData.getPreviousSongName();

        return previousSong;
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


    public void refreshOnAirData(String bandName, String albumName, String songName) {
        onAirData.refreshRegisteredBandsIndex(bandName);
        onAirData.refreshActualBandAlbumsIndex(albumName);
        onAirData.refreshActualAlbumTrackListIndex(songName);

        Long bandId = bandRepository.getIdByName(bandName);
        onAirData.setBandOnAirId(bandId);
        Long albumId = albumRepository.getAlbumIdByName(albumName);
        onAirData.setAlbumOnAirId(albumId);
        Long songId = songRepository.getSongIDBySongName(albumId, songName);
        onAirData.setSongOnAirId(songId);
    }

    public void play() {
        Song song = songRepository.getSongBySongId(onAirData.getSongOnAirId());
        try {
            FileInputStream fileInputStream = new FileInputStream(song.getPathToSong());
            player = new Player(fileInputStream);
            Runnable runnable = () -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            };
            playerThread = new Thread(runnable);
            playerThread.start();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void modifiedPlay() {
        stopSongOnAir();
        List<String> songnames = onAirData.getActualAlbumTrackList();
        List<String> songPaths = new ArrayList<>();

        for(int i = 0; i < songnames.size(); i++){
            songPaths.add(songRepository.getSongPathBySongName(songnames.get(i)));
            System.out.println(songPaths.get(i));
        }
        testThread = new Thread(() -> playList(songPaths));
        testThread.start();
    }

    private void playList(List<String> songPaths) {
        FileInputStream fis;
        for (int i = 0; i < songPaths.size(); i++){
            try {
                fis = new FileInputStream(songPaths.get(i));
                player = new Player(fis);
                Runnable runnable = () -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                };
                playerThread = new Thread(runnable);
                playerThread.start();
                do {
                    Thread.sleep(10);
                    System.out.println("Slow");
                }while (!player.isComplete());
            } catch (FileNotFoundException | JavaLayerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void stopSongOnAir() {
        if (player != null) {
            player.close();
            playerThread.interrupt();
        }
    }


    // Getters and setters
    public OnAirData getOnAirData() {
        return onAirData;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Thread getPlayerThread() {
        return playerThread;
    }

    public void setPlayerThread(Thread playerThread) {
        this.playerThread = playerThread;
    }


}
