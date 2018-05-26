package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Artist;
import com.ReAct.MusicReAct.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("artistRepository")
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist getByName(String name);

    @Query("select a from Artist a where a.name like %?1% OR a.biography like %?1%")
    Page<Artist> liteSearch(String query, Pageable pageable);

    @Query("select a from Artist a where a.biography IS NULL AND (a.name like %?1% OR a.biography like %?1%)")
    Page<Artist> liteSearchBiography(String query, Pageable pageable);

    Page findAllByBiographyIsNotNull(Pageable pageable);

}