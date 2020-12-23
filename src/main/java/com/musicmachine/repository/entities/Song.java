package com.musicmachine.repository.entities;

import javax.persistence.*;

@Entity
//@Table(name = "Song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SongName")
    private String songName;

    @Column(name = "PathToSong")
    private String pathToSong;

    @ManyToOne
    private Album album;

    public Song() {}

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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
