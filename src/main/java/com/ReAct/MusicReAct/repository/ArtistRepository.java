package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Artist;
import com.ReAct.MusicReAct.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("artistRepository")
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist getByName(String name);

    @Query("select a from Artist a where a.name like %?1% OR a.biography like %?1%")
    Page<Artist> liteSearch(String query, Pageable pageable);

    @Query("select a from Artist a where a.biography IS NULL AND (a.name like %?1% OR a.biography like %?1%)")
    Page<Artist> liteSearchBiography(String query, Pageable pageable);

    Page findAllByBiographyIsNotNull(Pageable pageable);


    List<Artist> findAllByBiographyIsNotNull();

    @Query("select a from Artist a left join Gallery g on a.id = g.artist WHERE a.id = g.artist group by a.id")
    Page<Artist> findAllWhereHasGallery(Pageable pageable);


    @Query("select a from Artist a left join Gallery g on a.id = g.artist WHERE a.id = g.artist and a.user = ?1 group by a.id")
    Page<Artist> findAllWhereHasGalleryUser(User user, Pageable pageable);

    Artist getByUserAndId(User user, Integer id);
    @Query("select a from Artist a where a.name like ?1%")
    List<Artist> findAllBy(String by);

    List<Artist> findAllByUser(User user);

    Page<Artist> findAllByUser(User user, Pageable pageable);

    List<Artist> findAllByUserAndBiographyIsNotNull(User user);

    Page<Artist> findAllByUserAndBiographyIsNotNull(User user, Pageable pageable);

    @Query("Select a from Track t left join Artist a  on a.id = t.artist where t.isVerified = 1 GROUP BY a.id Order by SUM(t.downloads + t.listened + t.views) DESC")
    Page<Artist> tops(Pageable pageable);


    @Query("Select a from Track t left join Artist a  on a.id = t.artist where t.isVerified = 1 GROUP BY a.id Order by SUM(t.downloads + t.listened + t.views) DESC")
    List<Artist> tops();




    @Query("select a from Artist a where a.name like %?1% OR a.biography like %?1%")
    List<Artist> searchArtists(String query);

    List<Artist> findTop50ByUserOrderByIdDesc(User user);

}