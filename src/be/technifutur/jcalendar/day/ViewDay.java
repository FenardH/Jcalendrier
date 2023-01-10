package be.technifutur.jcalendar.day;

import be.technifutur.jcalendar.*;
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

    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;

    public void displayDaySchedule (JcalendarModel model, int daysToSubstractOrAdd) throws JcalendarTimeConflictException {
        LocalDate date = model.minusOrPlusDays(daysToSubstractOrAdd);
        // head
        System.out.printf(headerFormat, "admin",
                                        date,
                                        Text.resizeString(jcalendar.getDayFromDate(date), 9, "left"));
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
                                        jcalendar.localDateToString(date),
                                        Text.resizeString(jcalendar.getDayFromDate(date), 9, "left"));
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
}
