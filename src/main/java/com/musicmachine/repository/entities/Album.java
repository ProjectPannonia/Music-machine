package com.musicmachine.repository.entities;

import javax.persistence.*;
import java.util.List;

@Entity
//@Table(name = "Album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AlbumName")
    private String albumName;

    @ManyToOne
    private Author author;

    @OneToMany(mappedBy = "album")
    private List<Song> songs;

    public Album() {}

    public Album(String albumName, Author author, List<Song> songs) {
        this.albumName = albumName;
        this.author = author;
        this.songs = songs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
