package be.technifutur.jcalendar.week;

import be.technifutur.jcalendar.Activity;
import be.technifutur.jcalendar.Jcalendar;
import be.technifutur.jcalendar.JcalendarModel;
import be.technifutur.jcalendar.JcalendarTimeConflictException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

public class ViewWeek {
    private String headerFormat = """
                      +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                      +      Lundi     +     Mardi      +    Mercredi    +      Jeudi     +    Vendredi    +     Samedi     +    Dimanche    +
                      +   .   +   .   +   .   +   .   +   .   +   .   +   .   +
                +-----+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                """.replaceAll("\\.","%s");

    private String body = """
                |  6h | . | . | . | . | . | . | . |
                |  7h | . | . | . | . | . | . | . |
                |  8h | . | . | . | . | . | . | . |
                |  9h | . | . | . | . | . | . | . |
                | 10h | . | . | . | . | . | . | . |
                | 11h | . | . | . | . | . | . | . |
                | 12h | . | . | . | . | . | . | . |
                +-----+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                | 13h | . | . | . | . | . | . | . |
                | 14h | . | . | . | . | . | . | . |
                | 15h | . | . | . | . | . | . | . |
                | 16h | . | . | . | . | . | . | . |
                | 17h | . | . | . | . | . | . | . |
                | 18h | . | . | . | . | . | . | . |
                | 19h | . | . | . | . | . | . | . |
                | 20h | . | . | . | . | . | . | . |
                | 21h | . | . | . | . | . | . | . |
                | 22h | . | . | . | . | . | . | . |
                | 23h | . | . | . | . | . | . | . |
                | 24h | . | . | . | . | . | . | . |
                +-----+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                |  1h | . | . | . | . | . | . | . |
                |  2h | . | . | . | . | . | . | . |
                |  3h | . | . | . | . | . | . | . |
                |  4h | . | . | . | . | . | . | . |
                |  5h | . | . | . | . | . | . | . |
                +-----+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                """.replaceAll("\\.","%s");

    public void displayWeekSchedule (JcalendarModel model, int plusMinusWeeks) throws JcalendarTimeConflictException {
        String[] week;
        if (plusMinusWeeks == 0) {
            LocalDate date = model.getDate();
            List<LocalDate> wd = Jcalendar.getWholeWeek(date);
            String[] currentWeekDays = Jcalendar.convertDateFormatForOneWeek(wd);
            week = Jcalendar.highlightToday(currentWeekDays);
        } else {
            week = Jcalendar.getPastFutureWeeksDates(plusMinusWeeks);
        }

        System.out.printf(headerFormat, week[0], week[1], week[2], week[3], week[4], week[5], week[6]);

        String[][] tab = new String[7][24];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }

        for (int i = 0; i < 7; i++) {
            JcalendarModel day = new JcalendarModel(Jcalendar.stringToLocalDate(week[i]));
            SortedSet<Activity> activities = day.getActivitiesInTheDay();
            if (activities != null) {
                for (Activity activity : activities) {
                    int indexX = i;
                    int indexY = Jcalendar.getTimeIntervalIndex(activity.startTime());
                    String content = activity.activityTitle();
                    tab[indexX][indexY] = content.length() > 14 ? content.substring(0, 11) + "..." : String.format("%" + (-14) + "s", content);
                }
            }
        }

        String[] flatTab = tab.stream()
                              .flatMap(list -> list.stream())
                              .collect(Collectors.toList());
        System.out.printf(body, flatTab);
    }

    public static void main(String[] args) throws JcalendarTimeConflictException {
            ViewWeek w = new ViewWeek();

            w.displayWeekSchedule(new JcalendarModel(Jcalendar.stringToLocalDate("03/01/2023")), -2);
        }
}
