package com.ReAct.MusicReAct.repository;

import com.ReAct.MusicReAct.model.Image;
import com.ReAct.MusicReAct.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image getByFileName(String fileName);
}