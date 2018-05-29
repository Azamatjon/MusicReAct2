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

    @Column(name = "old_file_name")
    private String oldFileName;

    @Column(name = "is_verified", nullable = false)
    private int isVerified = 0;

    @Column(name = "bitrate")
    private int bitrate;

    @Column(name = "duration")
    private long duration;

    @Column(name = "sample_rate", nullable = true)
    private int sampleRate;

    @Column(name = "size")
    private long size;

    @Column(name = "year")
    private int year;

    @Column(name = "views", nullable = false)
    private int views = 0;

    @Column(name = "listened", nullable = false)
    private int listened = 0;

    @Column(name = "downloads", nullable = false)
    private int downloads = 0;

    @ManyToOne(targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "artist_id")
    private Artist artist;

    @ManyToOne(targetEntity = Album.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "album_id")
    private Album album;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean getIsVerified() {
        return isVerified == 1;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = ((isVerified) ? 1:0);
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getFormattedDuration() {
        return (String.format("%02d", duration/60) + ":" + String.format("%02d", duration%60));
    }

    public String getFormattedSize() {
        return (String.format("%.2f", ((size > 0) ? (size / 1024.0 / 1024.0) : 0.0)) + " MB");
    }


    public String getImage() {
        if (album != null) {
            return album.getImage();
        } else {
            return "/albumImages/default_avatar.png";
        }
    }

    public String getListenLink() {
        return "/listenMusic/" + fileName;
    }

    public String getDownloadLink() {
        return "/downloadMusic/" + fileName;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void incrementViews(int views) {
        this.views++;
    }

    public void setListened(int listened) {
        this.listened = listened;
    }

    public void incrementListened() {
        this.listened++;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public void incrementDownloads() {
        this.downloads++;
    }
}