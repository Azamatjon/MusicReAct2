package com.ReAct.MusicReAct.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @ManyToOne(targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "artist_id")
    private Artist artist;

    @OneToMany()
    private Set<Track> tracks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return "/albumImages/" + ((image != null)?image:"default_avatar.png");
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public int getNumberOfTracks() {
        return tracks.size();
    }

}