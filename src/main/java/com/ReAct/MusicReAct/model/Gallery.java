package com.ReAct.MusicReAct.model;


        import org.jsoup.Jsoup;

        import javax.persistence.*;
        import java.sql.Date;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Set;

@Entity
@Table(name = "gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "file_name")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "artist_id")
    private Artist artist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getImage(){
        return (fileName != null) ? "/galleryImages/" + fileName : "/galleryImages/default_avatar.png";
    }
}