package dv.project.movienight.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MoviesOmdb {
    @JsonProperty("Search")
    private List<MovieOmdb> movies;

    public MoviesOmdb(){

    }

    public List<MovieOmdb> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieOmdb> movies) {
        this.movies = movies;
    }
}
