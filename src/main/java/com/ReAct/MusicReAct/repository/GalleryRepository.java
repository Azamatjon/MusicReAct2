package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Artist;
import com.ReAct.MusicReAct.model.Gallery;
import com.ReAct.MusicReAct.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("galleryRepository")
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    Page findAllByArtist(Artist artist, Pageable pageable);
    List<Gallery> findAllByArtist(Artist artist);
    List<Gallery> findAllByArtistAndUser(Artist artist, User user);
    Page findAllByArtistAndUser(Artist artist, User user, Pageable pageable);

    Gallery getByIdAndUser(Integer id, User user);

}