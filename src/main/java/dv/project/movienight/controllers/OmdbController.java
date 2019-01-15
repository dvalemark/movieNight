package dv.project.movienight.controllers;


import dv.project.movienight.entities.Movie;
import dv.project.movienight.entities.MovieOmdb;
import dv.project.movienight.entities.MoviesOmdb;
import dv.project.movienight.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class OmdbController {

    @Autowired
    private MovieRepository movieRepository;

    private static final String API_REQUEST = "http://www.omdbapi.com/?apikey=bd1f8ab4";
    private static final Logger log = LoggerFactory.getLogger(OmdbController.class);
    private RestTemplate restTemplate = new RestTemplate();
    private AtomicLong counter = new AtomicLong();

    String value = "home";


    @RequestMapping(value = "/searchMovie/{value}", method = RequestMethod.GET)
    public List<MovieOmdb> searchByName(@PathVariable String value){
        MoviesOmdb response  = restTemplate.getForObject(API_REQUEST+"&s="+ value, MoviesOmdb.class);
        response.getMovies().forEach(movie -> log.info(movie.toString()));
        counter.incrementAndGet();
        return response.getMovies();
    }

    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public Movie getById(@PathVariable String id){
        if(movieRepository.findByImdbId(id) == null){
            MovieOmdb responseMovie = restTemplate.getForObject(API_REQUEST+"&i="+id, MovieOmdb.class);
            log.info(responseMovie.toString());
            counter.incrementAndGet();
            addMovieToDB(responseMovie);
        }
        return movieRepository.findByImdbId(id);
    }

    public AtomicLong getCounter() {
        return counter;
    }

    private void addMovieToDB(MovieOmdb responseMovie){
        movieRepository.save(new Movie(responseMovie.getImdbId(), responseMovie.getTitle(), responseMovie.getPoster(), responseMovie.getRuntime()));
    }
}
