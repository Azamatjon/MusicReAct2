package com.ReAct.MusicReAct.model;


import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "biography", length = 999999999)
    private String biography;

    @Column(name = "birth_date")
    private Date birthDate;

    @OneToMany()
    private Set<Track> tracks;

    @OneToMany()
    private Set<Album> albums;

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
        return "/artistImages/" + ((image != null)?image:"default_avatar.png");
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getNumberOfTracks(){
        return tracks.size();
    }

    public int getNumberOfAlbums(){
        return albums.size();
    }
}