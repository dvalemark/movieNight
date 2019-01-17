package dv.project.movienight.OMDBAPI;

import dv.project.movienight.controllers.OmdbController;
import dv.project.movienight.entities.Movie;
import dv.project.movienight.entities.MovieOmdb;
import dv.project.movienight.entities.MoviesOmdb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class OMDBAPI {

    private static final String API_REQUEST = "http://www.omdbapi.com/?apikey=bd1f8ab4";
    private static final Logger log = LoggerFactory.getLogger(OmdbController.class);
    private RestTemplate restTemplate = new RestTemplate();

    public List<MovieOmdb> searchByName( String value){
        MoviesOmdb response  = restTemplate.getForObject(API_REQUEST+"&s="+ value + "*", MoviesOmdb.class);
        List<MovieOmdb> movies;
        try {
            movies = response.getMovies();
        }
        catch (Exception e){
            movies = null;
        }
        return movies;
    }

    public MovieOmdb searchById(String id){
        MovieOmdb responseMovie = restTemplate.getForObject(API_REQUEST+"&i="+id, MovieOmdb.class);

        return responseMovie;
    }
}
