package be.technifutur.jcalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Activity implements Comparable<Activity>, Serializable {
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    String activityTitle;
    String location;
    ActivityType type;
    Double tarif;

    public Activity(LocalDate date, LocalTime startTime, LocalTime endTime, String activityTitle, String location, ActivityType type, double tarif) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityTitle = activityTitle;
        this.location = location;
        this.type = type;
        this.tarif = tarif;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    @Override
    public String toString() {
        return startTime.toString() + "-" +
               endTime.toString() + " " +
               date.toString() + ", " +
               "Activité: " + activityTitle + ", " +
               "Local: " + location + "," +
               "Type: " + type.toString().toLowerCase() + "," +
               "Tarif: " + tarif +"\n"
               ;
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
                        if (compare == 0) {
                            compare = this.type.compareTo(a2.type);
                            if (compare == 0) {
                                compare = this.tarif.compareTo(a2.tarif);
                            }
                        }
                    }
                }
            }
        }
        return compare;
    }
}
