package com.ReAct.MusicReAct.repository;

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
    Page<Track> findAllByIsVerified(int i, Pageable pageable);

}