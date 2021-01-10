package com.musicmachine.repository.entities;

import javax.persistence.*;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SongName")
    private String songName;

    @Column(name = "PathToSong")
    private String pathToSong;

    @Column(name = "AlbumId")
    private Long albumId;

    public Song() {}

    public Song(String songName, String pathToSong, Long albumId) {
        this.songName = songName;
        this.pathToSong = pathToSong;
        this.albumId = albumId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPathToSong() {
        return pathToSong;
    }

    public void setPathToSong(String pathToSong) {
        this.pathToSong = pathToSong;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    //    public Album getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(Album album) {
//        this.album = album;
//    }
}
