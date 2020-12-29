package com.musicmachine.service;

import com.musicmachine.repository.AlbumRepository;
import com.musicmachine.repository.AuthorRepository;
import com.musicmachine.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {

    private List<String> authorNamesList;
    private int authorsListSize;
    private int authorsIndex;
    private Long authorOnAirId;

    private List<String> albumNamesList;
    private int albumListSize;
    private int albumIndex;
    private Long albumOnAirId;

    private List<String> songListNames;
    private int songListSize;
    private int songIndex;

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
        authorsIndex = 0;
        if(authorsListSize > 0) {
            authorOnAirId = authorRepository.getIdByName(authorNamesList.get(0));
            albumNamesList = albumRepository.findAlbumsByAuthorId(authorOnAirId);
            albumListSize = albumNamesList.size();
            if(albumListSize > 0){
                albumIndex = 0;
                albumOnAirId = albumRepository.getAlbumIdByName(albumNamesList.get(albumIndex));

                songListNames = songRepository.getSongsByAlbumId(albumOnAirId);
                System.out.println("Song list names size: " + songListNames.size());
                songListSize = songListNames.size();
                if(songListSize > 0) {
                    songIndex = 0;
                }
            }
            Collections.sort(albumNamesList);
        }
    }

    public String getFirstBandName() {
        String firstBandName = "Database empty";

        if (!authorNamesList.isEmpty()) firstBandName = authorNamesList.get(authorsIndex);

        return firstBandName;
    }

    public String getFirstAlbumFromThisAuthor() {
        String firsAlbumName = "Albums empty";
        //List<String> albums = albumRepository.findAlbumsByAuthorId(authorOnAirId);
        if (!albumNamesList.isEmpty()) firsAlbumName = albumNamesList.get(albumIndex);
        return firsAlbumName;
    }

    public String getFirstSongfFromAlbum(String actualAlbumName) {
        String firstSongName = "No registered songs.";
        Long albumId = albumRepository.getAlbumIdByName(actualAlbumName);
        songListNames = songRepository.getSongsByAlbumId(albumId);

        if (!songListNames.isEmpty()) firstSongName = songListNames.get(songIndex);
        return firstSongName;
    }

    public String giveNextBandName() {
        if(authorsIndex + 1 < authorsListSize) authorsIndex++;
        return authorNamesList.get(authorsIndex);
    }

    public String givePreviousBandName() {
        if(authorsIndex - 1 >= 0) authorsIndex--;
        return authorNamesList.get(authorsIndex);
    }

    public String giveNextAlbum() {
        if (albumIndex + 1 < albumListSize) albumIndex++;
        return albumNamesList.get(albumIndex);
    }

    public String givePreviousAlbum() {
        if(albumIndex -1 >= 0) albumIndex--;
        return albumNamesList.get(albumIndex);
    }

    public String giveNextSong() {
        if(songIndex + 1 < songListSize) songIndex++;
        return songListNames.get(songIndex);
    }

    public String givePreviousSong() {
        if(songIndex - 1 >= 0) songIndex--;
        return songListNames.get(songIndex);
    }
}
