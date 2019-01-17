package dv.project.movienight.GoogleAPI;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import dv.project.movienight.entities.GoogleUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EventAdder {

    //ToDo: Should i add 1 event/calendar or 1 event with all attendees?

    public String addEvent(Calendar calendar, String calendarName, List<GoogleUser> emailList, DateTime startTime, DateTime finishTime, String title) throws IOException {

        Event event = new Event()
                .setSummary("Watch "+title)
                .setDescription("Movie Night with friends");

        EventDateTime start = new EventDateTime()
                .setDateTime(startTime)
                .setTimeZone("GMT +01:00");

        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(finishTime)
                .setTimeZone("GMT +01:00");

        event.setEnd(end);

        event.setAttendees(Arrays.asList(addEventAttendees(emailList)));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("popup").setMinutes(10),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = calendarName;

                calendar.events()
                .insert(calendarId, event)
                .execute();


        return "OK";
    }

    public EventAttendee[] addEventAttendees(List<GoogleUser>emailList){
        EventAttendee[] attendees = new EventAttendee[emailList.size()];
        int i=0;
        for(GoogleUser user : emailList) {
            attendees[i] = new EventAttendee().setEmail(user.getEmail());
            i++;
        }
        return attendees;
    }
}
