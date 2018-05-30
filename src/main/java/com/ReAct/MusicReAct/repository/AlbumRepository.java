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

@Repository("albumRepository")
public interface AlbumRepository extends JpaRepository<Album, Integer> {
        Album getByNameAndArtist(String name, Artist artist);

        List<Album> findAllByArtist(Artist artist);


        List<Album> findTop4ByOrderByIdDesc();

        List<Album> findAllByUser(User user);

        Album getByUserAndId(User user, Integer id);

        Page<Album> findAllByUser(User user, Pageable pageable);


        @Query("Select a from Track t left join Album a  on a.id = t.album where t.isVerified = 1 GROUP BY a.id Order by SUM(t.downloads + t.listened + t.views) DESC")
        Page<Album> tops(Pageable pageable);


        @Query("Select a from Track t left join Album a  on a.id = t.album where t.isVerified = 1 GROUP BY a.id Order by SUM(t.downloads + t.listened + t.views) DESC")
        List<Album> tops();

        @Query("select a from Album a where a.name like %?1%")
        List<Album> searchAlbums(String query);

        List<Album> findTop50ByUserOrderByIdDesc(User user);
}