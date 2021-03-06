package dv.project.movienight.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import dv.project.movienight.GoogleAPI.EventAdder;
import dv.project.movienight.GoogleAPI.GoogleAPIHelper;
import dv.project.movienight.GoogleAPI.MovieTime;
import dv.project.movienight.entities.GoogleUser;
import dv.project.movienight.repositories.GoogleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.api.client.util.DateTime.parseRfc3339;
import static java.time.ZoneOffset.UTC;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GoogleController {
    GoogleAPIHelper googleAPIHelper = new GoogleAPIHelper();


    @Autowired
    private GoogleUserRepository googleUserRepository;

    String CLIENT_ID="108458082317-bdsimcu84edbgn4nvsok2kl3q1u89uin.apps.googleusercontent.com";
    String CLIENT_SECRET ="BuaD3dJiDHTW1Peb4wtgr7Vu";

    @RequestMapping(value = "/storeauthcode", method = POST)
    public String storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return "Error, wrong headers";
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    "http://localhost:8080")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store these 3in your DB
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresAt = System.currentTimeMillis()/1000 + (tokenResponse.getExpiresInSeconds());

        // Debug purpose only
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Use THIS ID as a key to identify a google user-account.
        String userId = payload.getSubject();

        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");


        if(googleUserRepository.findByUserId(userId)==null) {
            googleUserRepository.save(new GoogleUser(userId, email, accessToken, refreshToken, expiresAt));

        }
        else{
            System.out.println("User can only be added once");
        }

        return "OK";
    }

    @RequestMapping (value = "/addEvent/{movieTime}/{startTime}/{title}", method = POST)
    public String addingEvent(@PathVariable String movieTime, @PathVariable String startTime, @PathVariable String title  ) throws IOException {
        String nrMinutes =movieTime.substring(0,3);
        int minutes = Integer.valueOf(nrMinutes);
        DateTime start = new DateTime(startTime);
        long end = start.getValue()+minutes*60*1000;
        DateTime endTime = new DateTime(end);


        EventAdder eventAdder = new EventAdder();
        List<GoogleUser> emailList = googleUserRepository.getAllByUserIdNotNull();
        for ( GoogleUser user: emailList) {
            eventAdder.addEvent(googleAPIHelper.getCalendar(user), "primary", emailList, start, endTime, title);
        }
        return "OK";
    }

    @RequestMapping(value = "/getFreeTimes", method = GET)
    public ResponseEntity<List<LocalDateTime>> getFreeTimes() {
        List<LocalDateTime> times = new ArrayList<>();

        List<Event> events = new ArrayList<>();
        for (GoogleUser user: googleUserRepository.getAllByUserIdNotNull()) {
            Calendar cal = googleAPIHelper.getCalendar(user);
            Events responseEvents = googleAPIHelper.getEvents(cal);
            if(responseEvents!=null) {
                events = Stream.concat(events.stream(), responseEvents.getItems().stream())
                        .collect(Collectors.toList());
            }
            }
        LocalDateTime startTimeLocalDate = LocalDate.now().atTime(19, 00);
        LocalDateTime endTimeLocalDate = LocalDate.now().atTime(23, 00);
        long startTime = startTimeLocalDate.toInstant(UTC).toEpochMilli();
        long endTime = endTimeLocalDate.toInstant(UTC).toEpochMilli();
        int i = 0;
        long day = 86400000;
        while( i < 7){

            long startValue = startTime + i*day;
            long endValue = endTime + i*day;

            if(events == null){
                times.add(Instant.ofEpochMilli(startValue).atZone(ZoneOffset.UTC).toLocalDateTime() );
            }
             else if( googleAPIHelper.checkFreeTime(events, startValue, endValue)){
                LocalDateTime s = Instant.ofEpochMilli(startValue).atZone(ZoneOffset.UTC).toLocalDateTime();
             //   LocalDateTime e = Instant.ofEpochMilli(endValue).atZone(ZoneOffset.UTC).toLocalDateTime();
                times.add(s);
            }
            i++;
        }

        if (times.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(times, HttpStatus.OK);
    }

}
