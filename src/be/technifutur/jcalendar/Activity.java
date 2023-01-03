package be.technifutur.jcalendar;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record Activity(LocalDate date, LocalTime startTime, LocalTime endTime, String activityTitle, String location, ActivityType type, boolean isPresent) implements Comparable<Activity>{
    @Override
    public String toString() {
        return startTime.toString() + "-" +
               endTime.toString() + " " +
               date.toString() + ", " +
               "Activité: " + activityTitle + ", " +
               "Local: " + location + ", " +
               "Présence: " + ((isPresent) ? "Oui" : "Non") + "\n";
    }

    public int compareTo(Activity a2) {
        int compare = this.date.compareTo(a2.date);
        if (compare == 0) {
            compare = this.startTime.compareTo(a2.startTime);
            if (compare == 0) {
                compare = this.endTime.compareTo(a2.endTime);
                if (compare == 0) {
                    compare = this.activityTitle.compareTo(a2.activityTitle);
                    if (compare == 0) {
                        compare = this.location.compareTo(a2.location);
                    }
                }
            }
        }
        return compare;
    }
}
