package dv.project.movienight.GoogleAPI;



import java.time.LocalDate;
import java.time.LocalDateTime;


public class MovieTime {
    LocalDateTime start;
    LocalDateTime end;

    public MovieTime(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String toStrings(){
        return "Start "+ start.toString()+ " End "+end.toString();
    }

}
