package be.technifutur.jcalendar;

import java.sql.Date;
import java.sql.Time;

public record Activity(String date, String startTime, String endTime, Person person, String activityTitle, String location, ActivityType type, boolean isPresent) {
    @Override
    public String toString() {
        return startTime + "-" +
               endTime + " " +
               date + ", " +
               "Activité: " + activityTitle + ", " +
               "Personne: " + person.getPrenom() + " " + person.getNom() + ", " +
               "Local: " + location + ", " +
               "Présence: " + ((isPresent) ? "Oui" : "Non") + ", " +"\n";
    }
}
