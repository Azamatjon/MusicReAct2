package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Album;
import com.ReAct.MusicReAct.model.Artist;
import com.ReAct.MusicReAct.model.Track;
import com.ReAct.MusicReAct.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("trackRepository")
public interface TrackRepository extends JpaRepository<Track, Integer> {

    List<Track> findAllByUserAndIsVerified(User user, int i);

    Page<Track> findAllByUserAndIsVerified(User user, int i, Pageable pageable);

    Track getByUserAndId(User user, Integer id);

    Page<Track> findAllByIsVerified(int i, Pageable pageable);

    List<Track> findAllByIsVerified(int i);

    List<Track> findAllByAlbumAndIsVerified(Album album, int i);

    List<Track> findAllByArtistAndIsVerified(Artist artist, int i);

    Track findByFileName(String fileName);

    List<Track> findTop10ByArtistAndIsVerifiedOrderByIdDesc(Artist artist, int i);


    @Query("select t from Track t left join Artist a on t.artist = a.id where t.name like %?1% OR a.name like %?1%")
    List<Track> searchTracks(String query);

    @Query("Select t from Track t where t.isVerified = 1 Order by t.downloads DESC, t.listened DESC, t.views DESC")
    Page<Track> tops(Pageable pageable);


    @Query("Select t from Track t where t.isVerified = 1 Order by t.downloads DESC, t.listened DESC, t.views DESC")
    List<Track> tops();



    // Last 50 tracks
    List<Track> findTop50ByIsVerifiedOrderByIdDesc(int i);

    List<Track> findTop50ByUserAndIsVerifiedOrderByIdDesc(User user, int i);

}