package com.ReAct.MusicReAct.model;

import javax.persistence.*;

@Entity
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "bitrate")
    private int bitrate;

    @Column(name = "duration")
    private int duration;

    @Column(name = "size")
    private int size;

    @ManyToOne(targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "artist_id")
    private Artist artist;

    @ManyToOne(targetEntity = Album.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "album_id")
    private Album album;

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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}