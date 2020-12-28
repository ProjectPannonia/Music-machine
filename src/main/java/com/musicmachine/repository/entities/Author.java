package com.musicmachine.repository.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AuthorName")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private List<Album> albums;

    public Author() {}

    public Author(String authorName) {
        this.authorName = authorName;
    }

    public Author(String authorName, List<Album> albums) {
        this.authorName = authorName;
        this.albums = albums;
    }

    public void addAlbumToAlbums(Album album) {
        this.albums.add(album);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
