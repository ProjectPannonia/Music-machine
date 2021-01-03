package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.BandRepository;
import com.musicmachine.repository.SongRepository;
import com.musicmachine.service.onair.OnAirData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdatedPlayerService {

    OnAirData onAirData;

    BandRepository bandRepository;
    AlbumRepository albumRepository;
    SongRepository songRepository;

    @Autowired
    public UpdatedPlayerService(BandRepository bandRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public OnAirData getOnAirData() {
        return onAirData;
    }

    public void initialize() {
        onAirData = new OnAirData();
        List<String> registeredBands = bandRepository.getAllAuthorNames();
        System.out.println(registeredBands);
        onAirData.setRegisteredBands(registeredBands);
        if(onAirData.getRegisteredBandsSize() > 0) {
            // Set the authorOnAirId
            Long newBandOnAirId = bandRepository.getIdByName(onAirData.giveFirstElement("author"));
            System.out.println("New band id: " + newBandOnAirId);
            onAirData.setBandOnAirId(newBandOnAirId);
            // Set albumNamesList by auhorOnAirId
            onAirData.setActualBandAlbums(albumRepository.findAlbumsByAuthorId(onAirData.getBandOnAirId()));
            // If albumNamesList not empty ->
            if(!onAirData.getActualBandAlbums().isEmpty()){
                // -> set albumOnAirId
                onAirData.setAlbumOnAirId(albumRepository.getAlbumIdByName(onAirData.giveFirstElement("album")));

                List<String> tracklist = songRepository.getSongsByAlbumId(onAirData.getAlbumOnAirId());
                System.out.println("Tracklist: " + tracklist);
                onAirData.setActualAlbumTrackList(tracklist);
                System.out.println("Initialized tracklist size: " + onAirData.getActualAlbumTrackListSize());
                if(!onAirData.getActualAlbumTrackList().isEmpty())
                    onAirData.setActualAlbumTrackListIndex(0);
            }
        }
    }

    public String nextBand() {
        String nextBand = onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());;
        if(onAirData.getRegisteredBandsIndex() + 1 < onAirData.getRegisteredBandsSize()) {
            nextBand = onAirData.getNextBandName();
            Long nextBandOnAirId = bandRepository.getIdByName(nextBand);
            onAirData.setBandOnAirId(nextBandOnAirId);
            refreshBandAlbumList(nextBandOnAirId);
        }
        return nextBand;
    }
    public String previousBand() {
        String previousBand = onAirData.getRegisteredBands().get(onAirData.getRegisteredBandsIndex());
        if(onAirData.getRegisteredBandsIndex() - 1 >= 0){
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
        if(onAirData.getActualBandAlbumsIndex() + 1 < onAirData.getActualBandAlbumsSize()){
            nextAlbum = onAirData.getNextAlbumName();
            Long nextAlbumOnAirId = albumRepository.getAlbumIdByName(nextAlbum);
            onAirData.setAlbumOnAirId(nextAlbumOnAirId);
            refreshSongList(nextAlbumOnAirId);
        }
        return nextAlbum;
    }
    public String givePreviousAlbum() {
        String previousAlbum = onAirData.getActualBandAlbums().get(onAirData.getActualBandAlbumsIndex());
        if(onAirData.getActualBandAlbumsIndex() - 1 >= 0) {
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

// TO - DO
    public String giveNextSong() {
        String nextSong = onAirData.getActualAlbumTrackList().get(onAirData.getActualAlbumTrackListIndex());

        if(onAirData.getActualAlbumTrackListIndex() + 1 < onAirData.getActualAlbumTrackListSize())
            nextSong = onAirData.getNextSongName();

        return nextSong;
    }

    public String givePreviousSong() {
        String previousSong = onAirData.getActualAlbumTrackList().get(onAirData.getActualAlbumTrackListIndex());
        if(onAirData.getActualAlbumTrackListIndex() - 1 >= 0)
            previousSong = onAirData.getPreviousSongName();

        return previousSong;
    }
}
