package dv.project.movienight.repositories;


import dv.project.movienight.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByTitleContaining(String title);
    Movie findByImdbId(String id);
}
