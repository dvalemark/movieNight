package dv.project.movienight.GoogleAPI;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import dv.project.movienight.entities.GoogleUser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleAPIHelper {
    String CLIENT_ID="108458082317-bdsimcu84edbgn4nvsok2kl3q1u89uin.apps.googleusercontent.com";
    String CLIENT_SECRET ="BuaD3dJiDHTW1Peb4wtgr7Vu";


    public Boolean accesTokenHasExpired(Long expiresAt){
        Long currentTime = System.currentTimeMillis()/1000;
        if( expiresAt>currentTime){return false;}
        return true;
    }

    public String getNewAccessToken(String refreshToken){
        GoogleCredential credential = getRefreshedCredentials(refreshToken);
        String accessToken = credential.getAccessToken();
        return accessToken;
    }

    private GoogleCredential getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), refreshCode,
                    CLIENT_ID,
                    CLIENT_SECRET)
                    .execute();

            return new GoogleCredential().setAccessToken(response.getAccessToken());
        }
        catch( Exception ex ){
            ex.printStackTrace();
            return null;
        }
    }



    public Calendar getCalendar(GoogleUser user){
        if(accesTokenHasExpired(user.getExpiresAt()))
        {
            user.setAccessToken(getNewAccessToken(user.getRefreshToken()));
        }

        GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
        Calendar calendar =
                new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Movie Nights")
                        .build();
    return calendar;
    }

    public Events getEvents(Calendar calendar) {
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime week = new DateTime(System.currentTimeMillis()+7*86400000);
        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setTimeMin(now)
                    .setTimeMax(week)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return events;
    }

    public boolean checkFreeTime(List<Event> events, long start, long end){
        if(events.size()==0){
            return true;
        }
        for (Event e: events) {
            boolean HASFULLDAYEVENT =e.getStart().getDateTime().isDateOnly();
            boolean HASEVENTSTARTINGDURINGTIME = e.getStart().getDateTime().getValue()>start && e.getStart().getDateTime().getValue()<end;
            boolean HASEVENTFINISHINGDURINGTIME = e.getEnd().getDateTime().getValue()>start && e.getEnd().getDateTime().getValue()<end;

            if(HASFULLDAYEVENT)return false;
            else if(HASEVENTSTARTINGDURINGTIME || HASEVENTFINISHINGDURINGTIME) return false;
            }

        return true;
    }


}
