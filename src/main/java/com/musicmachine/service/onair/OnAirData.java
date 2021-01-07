package com.musicmachine.service.onair;

import java.util.List;

public class OnAirData {

    private Long bandOnAirId;
    private List<String> registeredBands;
    private int registeredBandsSize;
    private int registeredBandsIndex;

    private Long albumOnAirId;
    private List<String> actualBandAlbums;
    private int actualBandAlbumsSize;
    private int actualBandAlbumsIndex;

    private Long songOnAirId;
    private List<String> actualAlbumTrackList;
    private int actualAlbumTrackListSize;
    private int actualAlbumTrackListIndex;

    public OnAirData() {
        this.registeredBandsIndex = 0;
        this.actualBandAlbumsIndex = 0;
        this.actualAlbumTrackListIndex = 0;
    }

    // Public methods
    public String getNextBandName() {
        if (registeredBandsIndex + 1 < registeredBandsSize) registeredBandsIndex++;
        return registeredBands.get(registeredBandsIndex);
    }

    public String getPreviousBandName() {
        if (registeredBandsIndex - 1 >= 0) registeredBandsIndex--;
        return registeredBands.get(registeredBandsIndex);
    }

    public String getNextAlbumName() {
        if (actualBandAlbumsIndex + 1 < actualBandAlbumsSize) actualBandAlbumsIndex++;
        return actualBandAlbums.get(actualBandAlbumsIndex);
    }

    public String getPreviousAlbumName() {
        if (actualBandAlbumsIndex - 1 >= 0) actualBandAlbumsIndex--;
        return actualBandAlbums.get(actualBandAlbumsIndex);
    }

    public String getNextSongName() {
        actualAlbumTrackListIndex++;
        return actualAlbumTrackList.get(actualAlbumTrackListIndex);
    }
    public String getPreviousSongName() {
        actualAlbumTrackListIndex--;
        return actualAlbumTrackList.get(actualAlbumTrackListIndex);
    }

    public String giveFirstElement(String listName) {
        String firstElement;
        switch (listName) {
            case "author":
                firstElement = registeredBands.get(0);
                break;
            case "album":
                firstElement = actualBandAlbums.get(0);
                break;
            case "song":
                firstElement = actualAlbumTrackList.get(0);
                break;
            default:
                firstElement = "Invalid request";
                break;
        }
        return firstElement;
    }

    public boolean hasNextSong() {
        return actualAlbumTrackListIndex < actualAlbumTrackListSize;
    }
    public void refreshRegisteredBandsIndex(String bandName) {
        if(registeredBands.contains(bandName)) registeredBandsIndex = registeredBands.indexOf(bandName);
    }
    public void refreshActualBandAlbumsIndex(String albumName){
        if(actualBandAlbums.contains(albumName)) actualBandAlbumsIndex = actualBandAlbums.indexOf(albumName);
    }
    public void refreshActualAlbumTrackListIndex(String songName) {
        if(actualAlbumTrackList.contains(songName)) actualAlbumTrackListIndex = actualAlbumTrackList.indexOf(songName);
    }
    public void increaseSongIndex() {
        actualAlbumTrackListIndex++;
    }

    // Getters and Setters
    public Long getBandOnAirId() {
        return bandOnAirId;
    }

    public void setBandOnAirId(Long bandOnAirId) {
        this.bandOnAirId = bandOnAirId;
    }

    public List<String> getRegisteredBands() {
        return registeredBands;
    }

    public void setRegisteredBands(List<String> registeredBands) {
        this.registeredBands = registeredBands;
        setRegisteredBandsSize(registeredBands.size());
    }

    public int getRegisteredBandsSize() {
        return registeredBandsSize;
    }

    public void setRegisteredBandsSize(int registeredBandsSize) {
        this.registeredBandsSize = registeredBandsSize;
    }

    public int getRegisteredBandsIndex() {
        return registeredBandsIndex;
    }

    public void setRegisteredBandsIndex(int registeredBandsIndex) {
        this.registeredBandsIndex = registeredBandsIndex;
    }

    public Long getAlbumOnAirId() {
        return albumOnAirId;
    }

    public void setAlbumOnAirId(Long albumOnAirId) {
        this.albumOnAirId = albumOnAirId;
    }

    public List<String> getActualBandAlbums() {
        return actualBandAlbums;
    }

    public void setActualBandAlbums(List<String> actualBandAlbums) {
        this.actualBandAlbums = actualBandAlbums;
        setActualBandAlbumsSize(actualBandAlbums.size());
    }

    public int getActualBandAlbumsSize() {
        return actualBandAlbumsSize;
    }

    public void setActualBandAlbumsSize(int actualBandAlbumsSize) {
        this.actualBandAlbumsSize = actualBandAlbumsSize;
    }

    public int getActualBandAlbumsIndex() {
        return actualBandAlbumsIndex;
    }

    public void setActualBandAlbumsIndex(int actualBandAlbumsIndex) {
        this.actualBandAlbumsIndex = actualBandAlbumsIndex;
    }

    public List<String> getActualAlbumTrackList() {
        return actualAlbumTrackList;
    }

    public void setActualAlbumTrackList(List<String> actualAlbumTrackList) {
        this.actualAlbumTrackList = actualAlbumTrackList;
        actualAlbumTrackListSize = actualAlbumTrackList.size();
    }

    public int getActualAlbumTrackListSize() {
        return actualAlbumTrackListSize;
    }

    public void setActualAlbumTrackListSize(int actualAlbumTrackListSize) {
        this.actualAlbumTrackListSize = actualAlbumTrackListSize;
    }

    public int getActualAlbumTrackListIndex() {
        return actualAlbumTrackListIndex;
    }

    public void setActualAlbumTrackListIndex(int actualAlbumTrackListIndex) {
        this.actualAlbumTrackListIndex = actualAlbumTrackListIndex;
    }

    public Long getSongOnAirId() {
        return songOnAirId;
    }

    public void setSongOnAirId(Long songOnAirId) {
        this.songOnAirId = songOnAirId;
    }

    @Override
    public String toString() {
        return "OnAirData{" +
                "bandOnAirId=" + bandOnAirId +
                ", registeredBands=" + registeredBands +
                ", registeredBandsSize=" + registeredBandsSize +
                ", registeredBandsIndex=" + registeredBandsIndex +
                ", albumOnAirId=" + albumOnAirId +
                ", actualBandAlbums=" + actualBandAlbums +
                ", actualBandAlbumsSize=" + actualBandAlbumsSize +
                ", actualBandAlbumsIndex=" + actualBandAlbumsIndex +
                ", songOnAirId=" + songOnAirId +
                ", actualAlbumTrackList=" + actualAlbumTrackList +
                ", actualAlbumTrackListSize=" + actualAlbumTrackListSize +
                ", actualAlbumTrackListIndex=" + actualAlbumTrackListIndex +
                '}';
    }
}
