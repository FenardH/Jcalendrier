package be.technifutur.jcalendar.day;

import be.technifutur.jcalendar.*;

import java.time.LocalDate;
import java.util.*;

public class ModelDay implements JcalendarModel {
    Set<Activity> activitiesInTheDay;

    public ModelDay(String date) {
        Optional<Set<Activity>> findActivities = getRecord(JcalendarDateTime.convertStringToLocalDate(date));
        this.activitiesInTheDay = findActivities.isPresent() ? findActivities.get() : null;
    }



    public static void main(String[] args) {
        JcalendarModel m = new ModelDay();

        m.addRecord(new Activity("30/12/2022", "09:00", "10:00", new Person("Johnson", "Boris"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        m.addRecord(new Activity("30/12/2022", "10:30", "11:30", new Person("Johnson", "Boris"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        m.addRecord(new Activity("31/12/2022", "09:00", "10:00", new Person("Henry", "Thierry"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        m.addRecord(new Activity("31/12/2022", "09:30", "10:30", new Person("Harry", "kane"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
        m.addRecord(new Activity("31/12/2022", "10:30", "11:30", new Person("Henry", "Thierry"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        LocalDate date = JcalendarDateTime.convertStringToLocalDate("30/12/2022");
        System.out.println(m.getRecord(date));
        System.out.println("------------");
        System.out.println(m.getRecord(ActivityType.SEANCE));
        System.out.println("------------");
        Person p = new Person("Johnson", "Boris");
        System.out.println(m.getRecord(p));
        System.out.println("------------");
        System.out.println(m.getRecord("Technifutur"));
        System.out.println("------------");
        System.out.println(m.getRecordsNumber("31/12/2022"));
        System.out.println("------------");
        System.out.println(m.getAllRecords());
        System.out.println("------------");
    }
}
