package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.service.onair.OnAirData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
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
    private OnAirData onAirData;



    BandRepository bandRepository;
    AlbumRepository albumRepository;
    SongRepository songRepository;

    @Autowired
    public PlayerService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public void initialize() {
        onAirData = new OnAirData();
        onAirData.setRegisteredBands(bandRepository.getAllAuthorNames());
        //authorNamesList = authorRepository.getAllAuthorNames();
        //authorsListSize = authorNamesList.size();
        //authorNamesListIndex = 0;
        if(onAirData.getRegisteredBandsSize() > 0) {
            onAirData.setBandOnAirId(bandRepository.getIdByName(authorNamesList.get(0)));
            //authorOnAirId = authorRepository.getIdByName(authorNamesList.get(0));
            onAirData.setActualBandAlbums(albumRepository.findAlbumsByAuthorId(authorOnAirId));
            //albumNamesList = albumRepository.findAlbumsByAuthorId(authorOnAirId);
            //albumListSize = albumNamesList.size();
            if(onAirData.getActualBandAlbumsSize() > 0){
                //albumNamesListIndex = 0;
                onAirData.setAlbumOnAirId(albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex)));
                //albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));

                //songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
                onAirData.setActualAlbumTrackList(songRepository.getSongsByAlbumId(albumOnAirId));
                System.out.println("Song list names size: " + songNamesList.size());
                //songListSize = songNamesList.size();
                if(onAirData.getActualAlbumTrackListSize() > 0) onAirData.setActualAlbumTrackListIndex(0);
            }
            //Collections.sort(albumNamesList);
        }
    }

    public String getFirstBandName() {
        String firstBandName = "Database empty";
        if(!onAirData.getRegisteredBands().isEmpty())
            firstBandName = onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
            //firstBandName = authorNamesList.get(authorNamesListIndex);
        //if (!authorNamesList.isEmpty()) firstBandName = authorNamesList.get(authorNamesListIndex);

        return firstBandName;
    }

    public String getFirstAlbumFromThisAuthor() {
        String firstAlbumName = "Albums empty";
        if(!onAirData.getActualBandAlbums().isEmpty())
            firstAlbumName = onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex());
            //firstAlbumName = albumNamesList.get(albumNamesListIndex);
        //if (!albumNamesList.isEmpty()) firstAlbumName = albumNamesList.get(albumNamesListIndex);
        return firstAlbumName;
    }

    public String getFirstSongfFromAlbum(String actualAlbumName) {
        String firstSongName = "No registered songs.";
        Long albumId = albumRepository.getAlbumIdByName(actualAlbumName);
        onAirData.setActualAlbumTrackList(songRepository.getSongsByAlbumId(albumId));
        //songNamesList = songRepository.getSongsByAlbumId(albumId);

        if(!onAirData.getActualAlbumTrackList().isEmpty())
            firstSongName = onAirData.getActualAlbumTrackList().get(onAirData.getActualBandAlbumsIndex());
            //firstSongName = songNamesList.get(songNamesListIndex);
        //if (!songNamesList.isEmpty()) firstSongName = songNamesList.get(songNamesListIndex);

        return firstSongName;
    }



    public String giveNextBandName() {
        if(onAirData.getRegisteredBandsIndex()+1 < onAirData.getRegisteredBandsSize()){
            //onAirData.increaseAuthorNamesListIndex();
            refreshAlbumNameListAndSongNamesList();
        }
//        if(authorNamesListIndex + 1 < authorsListSize) {
//            authorNamesListIndex++;
//            refreshAlbumNameListAndSongNamesList();
//        }
        return onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
        //return authorNamesList.get(authorNamesListIndex);
    }
    public String givePreviousBandName() {
        if (onAirData.getRegisteredBandsIndex()-1 >= 0) {
            //onAirData.decreaseAuthorNamesListIndex();
            refreshAlbumNameListAndSongNamesList();
        }
//        if(authorNamesListIndex - 1 >= 0) {
//            authorNamesListIndex--;
//            refreshAlbumNameListAndSongNamesList();
//        }
        return  onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
//        return authorNamesList.get(authorNamesListIndex);
    }
    // TO - DO refactor
    private void refreshAlbumNameListAndSongNamesList() {
        onAirData.setBandOnAirId(bandRepository.getIdByName(onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex())));
        //authorOnAirId = authorRepository.getIdByName(authorNamesList.get(authorNamesListIndex));
        // Refresh albums list
        onAirData.setActualBandAlbums(albumRepository.findAlbumsByAuthorId(onAirData.getBandOnAirId()));
        //albumNamesList = albumRepository.findAlbumsByAuthorId(authorOnAirId);
        onAirData.setActualBandAlbumsSize(onAirData.getActualBandAlbums().size());
        //albumListSize = albumNamesList.size();

        // Set albumNamesListIndex to 0
        onAirData.setActualBandAlbumsIndex(0);
        //albumNamesListIndex = 0;
        // Set albumOnAirId to the first album's albumId
        onAirData.setAlbumOnAirId(albumRepository.getAlbumIdByName(onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex())));
        //albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));
        // Refresh songs by albumId
        onAirData.setActualAlbumTrackList(songRepository.getSongsByAlbumId(onAirData.getAlbumOnAirId()));
        //songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
        onAirData.setActualAlbumTrackListSize(onAirData.getActualAlbumTrackList().size());
        //songListSize = songNamesList.size();
        onAirData.setActualAlbumTrackListIndex(0);
        //songNamesListIndex = 0;
    }
//    public String giveNextAlbum() {
//        //if(onAirData.getAlbumNamesListIndex()+1 < onAirData.getAlbumListSize()){
////        if(onAirData.albumHasNextSong()){
////            //onAirData.increaseAlbumNamesListIndex();
////            refreshSongNamesList();
////        }
////        if (albumNamesListIndex + 1 < albumListSize) {
////            albumNamesListIndex++;
////            refreshSongNamesList();
////        }
//        return onAirData.getAlbumNameOnAir();
//        //return albumNamesList.get(albumNamesListIndex);
//    }
//    public String givePreviousAlbum() {
//        if(onAirData.albumHasPreviousSong()){
//            onAirData.decreaseAlbumNamesListIndex();
//            refreshSongNamesList();
//        }
////        if(albumNamesListIndex -1 >= 0) {
////            albumNamesListIndex--;
////            refreshSongNamesList();
////        }
//        //return albumNamesList.get(albumNamesListIndex);
//        return onAirData.getAlbumNameOnAir();
//    }
//    private void refreshSongNamesList() {
//        // Set albumOnAirId to the next album's albumId
//        onAirData.setAlbumOnAirId(albumRepository.getAlbumIdByName(onAirData.getAlbumNameOnAir()));
//        //albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumNamesListIndex));
//        // Refresh songs by albumId
//        onAirData.setSongNamesList(songRepository.getSongsByAlbumId(onAirData.getAlbumOnAirId()));
//        //songNamesList = songRepository.getSongsByAlbumId(albumOnAirId);
//        onAirData.setSongNameListSize(onAirData.getSongNamesList().size());
//        //songListSize = songNamesList.size();
//        onAirData.setSongNamesListIndex(0);
//        //songNamesListIndex = 0;
//    }
    public String getBandFirstAlbum() {
        return onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex());
        //return albumNamesList.get(albumNamesListIndex);
    }
    public String getFirstSongNameFromAlbum() {
        return onAirData.getActualAlbumTrackList().get(onAirData.getActualAlbumTrackListIndex());
        //return songNamesList.get(songNamesListIndex);
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

    public void setAuthorNamesList(List<String> authorNamesList) {
        this.authorNamesList = authorNamesList;
    }

    public int getAuthorsListSize() {
        return authorsListSize;
    }

    public void setAuthorsListSize(int authorsListSize) {
        this.authorsListSize = authorsListSize;
    }

    public int getAuthorNamesListIndex() {
        return authorNamesListIndex;
    }

    public void setAuthorNamesListIndex(int authorNamesListIndex) {
        this.authorNamesListIndex = authorNamesListIndex;
    }

    public Long getAuthorOnAirId() {
        return authorOnAirId;
    }

    public void setAuthorOnAirId(Long authorOnAirId) {
        this.authorOnAirId = authorOnAirId;
    }

    public List<String> getAlbumNamesList() {
        return albumNamesList;
    }

    public void setAlbumNamesList(List<String> albumNamesList) {
        this.albumNamesList = albumNamesList;
    }

    public int getAlbumListSize() {
        return albumListSize;
    }

    public void setAlbumListSize(int albumListSize) {
        this.albumListSize = albumListSize;
    }

    public int getAlbumNamesListIndex() {
        return albumNamesListIndex;
    }

    public void setAlbumNamesListIndex(int albumNamesListIndex) {
        this.albumNamesListIndex = albumNamesListIndex;
    }

    public Long getAlbumOnAirId() {
        return albumOnAirId;
    }

    public void setAlbumOnAirId(Long albumOnAirId) {
        this.albumOnAirId = albumOnAirId;
    }

    public List<String> getSongNamesList() {
        return songNamesList;
    }

    public void setSongNamesList(List<String> songNamesList) {
        this.songNamesList = songNamesList;
    }

    public int getSongListSize() {
        return songListSize;
    }

    public void setSongListSize(int songListSize) {
        this.songListSize = songListSize;
    }

    public int getSongNamesListIndex() {
        return songNamesListIndex;
    }

    public void setSongNamesListIndex(int songNamesListIndex) {
        this.songNamesListIndex = songNamesListIndex;
    }
}
