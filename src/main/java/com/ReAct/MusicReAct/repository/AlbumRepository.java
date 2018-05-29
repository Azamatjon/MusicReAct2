package com.ReAct.MusicReAct.repository;

        import com.ReAct.MusicReAct.model.Album;
        import com.ReAct.MusicReAct.model.Artist;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Repository("albumRepository")
public interface AlbumRepository extends JpaRepository<Album, Integer> {
        Album getByNameAndArtist(String name, Artist artist);

        List<Album> findAllByArtist(Artist artist);

        List<Album> findTop4ByOrderByIdDesc();
}