package be.technifutur.jcalendar.day;

import be.technifutur.jcalendar.*;
import static be.technifutur.jcalendar.Jcalendar.today;

import java.time.LocalDate;
import java.util.Optional;
import java.util.SortedSet;

public class ViewDay {
    private final String headerFormat = """
            Utilisateur: %s
            +------------+
            + %s +
            +  %s +
            +------------+
            
            """;

    public void displayDaySchedule (JcalendarModel model, int daysToSubstractOrAdd) throws JcalendarTimeConflictException {
        LocalDate date = model.minusOrPlusDays(daysToSubstractOrAdd);
        // head
        System.out.printf(headerFormat, "admin",
                                        date,
                                        Text.resizeString(Jcalendar.localDateToString(date), 9, "left"));
        // body
        Optional<SortedSet<Activity>> activitiesInTheDay = model.getRecord(date);
        if (activitiesInTheDay.isPresent()) {
            SortedSet<Activity> actsDay = activitiesInTheDay.get();
            for (int i = 0; i < actsDay.size(); i ++) {
                System.out.printf("[%s] %s\n", i + 1, actsDay.toArray()[i]);
            }
        } else {
            System.out.println("Aucun historique trouvé !\n");
        }
    }

    public void displayDaySchedule (Person person, int daysToSubstractOrAdd) throws JcalendarTimeConflictException {
        LocalDate date = LocalDate.from(today);
        if (daysToSubstractOrAdd > 0) {
            date = date.plusDays(daysToSubstractOrAdd);
        } else if (daysToSubstractOrAdd < 0) {
            date = date.minusDays(-daysToSubstractOrAdd);
        }
        // head
        System.out.printf(headerFormat, person.getPrenom() + " " + person.getNom(),
                                        Jcalendar.localDateToString(date),
                                        Text.resizeString(Jcalendar.getDayFromDate(date), 9, "left"));
        // body
        Optional<SortedSet<Activity>> activitiesInTheDay = person.getActivity(date);
        if (activitiesInTheDay.isPresent()) {
            SortedSet<Activity> actsDay = activitiesInTheDay.get();
            for (int i = 0; i < actsDay.size(); i ++) {
                System.out.printf("[%s] %s\n", i + 1, actsDay.toArray()[i]);
            }
        } else {
            System.out.println("Aucun historique trouvé !\n");
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
