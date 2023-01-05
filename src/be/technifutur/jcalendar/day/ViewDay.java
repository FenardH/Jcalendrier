package be.technifutur.jcalendar.day;

import be.technifutur.jcalendar.*;

import java.util.Optional;
import java.util.SortedSet;

public class ViewDay {
    private final String headerFormat = """
            +------------+
            + %s +
            +  %s +
            +------------+
            """;

    public void displayDaySchedule (JcalendarModel model, int daysToSubstractOrAdd) throws JcalendarTimeConflictException {
        model.minusOrPlusDays(daysToSubstractOrAdd);

        System.out.printf(headerFormat, model.getDate(), Jcalendar.resizeString(model.getDay(), 9, "left"));
        System.out.println();
        SortedSet<Activity> activitiesInTheDay = model.getActivitiesInTheDay().orElse(null);
        if (activitiesInTheDay != null) {
            for (int i = 0; i < activitiesInTheDay.size(); i ++) {
                System.out.printf("[%s] %s\n", i + 1, activitiesInTheDay.toArray()[i]);
            }
        } else {
            System.out.println("Aucun historique trouvé !");
        }
    }

    public static void main(String[] args) throws JcalendarTimeConflictException {
        JcalendarModel testModel = new JcalendarModel();

        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Python Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "HTML Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("11:30"), Jcalendar.stringToLocalTime("12:30"), "Python Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("14:00"), Jcalendar.stringToLocalTime("15:00"), "Computer Science Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("09:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("15:30"), "Python Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("15:30"), Jcalendar.stringToLocalTime("17:30"), "C++ Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:30"), ".Net Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("12:00"), "TypeScript course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("14:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("14:30"), Jcalendar.stringToLocalTime("15:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:30"), ".Net Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("11:30"), Jcalendar.stringToLocalTime("12:00"), "TypeScript course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("14:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("16:00"), Jcalendar.stringToLocalTime("17:30"), "Java Course", "Technipaste", ActivityType.REPOS));

        ViewDay d = new ViewDay();
        d.displayDaySchedule(testModel, -2);
    }
}
