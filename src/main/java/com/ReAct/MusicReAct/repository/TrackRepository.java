package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("trackRepository")
public interface TrackRepository extends JpaRepository<Track, Integer> {

}