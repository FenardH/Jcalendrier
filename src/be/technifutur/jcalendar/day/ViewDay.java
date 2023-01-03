package be.technifutur.jcalendar.day;

import be.technifutur.jcalendar.*;

import java.util.SortedSet;

public class ViewDay {
    private String headerFormat = """
            +------------+
            + %s +
            +  %s +
            +------------+
            """;

    public void displayDaySchedule (JcalendarModel model) {
        System.out.printf(headerFormat, model.getDate(), String.format("%" + (-9) + "s", model.getDay()));
        System.out.println();
        SortedSet<Activity> activitiesInTheDay = model.getActivitiesInTheDay();
        if (activitiesInTheDay != null) {
            for (int i = 0; i < activitiesInTheDay.size(); i ++) {
                System.out.printf("[%s] %s\n", i + 1, model.getActivitiesInTheDay().toArray()[i]);
            }
        } else {
            System.out.println("Aucun historique trouvé !");
        }
    }

//    public static void main(String[] args) {
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:30"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("14:00"), Jcalendar.stringToLocalTime("15:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:30"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
//        ModelDay.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//
//        ViewDay d = new ViewDay();
//        ModelDay m = new ModelDay(Jcalendar.stringToLocalDate("04/01/2023"));
//        d.displayDaySchedule(m);
//    }
}
