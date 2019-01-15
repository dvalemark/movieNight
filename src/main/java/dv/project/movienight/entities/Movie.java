package dv.project.movienight.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Movie {
    @Id
    private String imdbId;

    private String title;

    private String poster;

    private String runtime;

    public Movie(){}

    public Movie(String imdbId, String title, String poster, String runtime) {
        this.imdbId = imdbId;
        this.title = title;
        this.poster = poster;
        this.runtime = runtime;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
