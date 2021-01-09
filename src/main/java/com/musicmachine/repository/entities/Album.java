package com.musicmachine.repository.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "AlbumName")
    private String albumName;

    @NotNull
    @Column(name = "AuthorId")
    private Long authorId;

    @Column(name = "CoverBackPath")
    private String coverBackPath;

    @Column(name = "CoverFrontPath")
    private String coverFrontPath;

    public Album() {}

    public Album(String albumName, Long authorId) {
        this.albumName = albumName;
        this.authorId = authorId;
    }

    public Album(String albumName, Long authorId, String coverFrontPath, String coverBackPath) {
        this.albumName = albumName;
        this.authorId = authorId;
        this.coverBackPath = coverBackPath;
        this.coverFrontPath = coverFrontPath;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getCoverBackPath() {
        return coverBackPath;
    }

    public void setCoverBackPath(String coverBackPath) {
        this.coverBackPath = coverBackPath;
    }

    public String getCoverFrontPath() {
        return coverFrontPath;
    }

    public void setCoverFrontPath(String coverFrontPath) {
        this.coverFrontPath = coverFrontPath;
    }
}
