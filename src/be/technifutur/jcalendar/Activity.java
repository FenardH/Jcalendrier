package be.technifutur.jcalendar;

import java.sql.Date;
import java.sql.Time;

public record Activity(Date date, Time startTime, Time endTime, Person person, String activityTitle, String location, String description, boolean isPresent, int isRepeat) {

}
