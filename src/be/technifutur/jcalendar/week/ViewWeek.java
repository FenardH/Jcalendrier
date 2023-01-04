package be.technifutur.jcalendar.week;

import be.technifutur.jcalendar.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Stream;

public class ViewWeek {
    private final String headerFormat = """
                      +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                      +     .  +     .  +    .    +     .  +    .   +     .  +    .   +
                      +   .   +   .   +   .   +   .   +   .   +   .   +   .   +
                +-----+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                """.replaceAll("\\.","%s");

    private final String bodyFormat = """
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

    public void displayWeekSchedule (JcalendarModel model, int weeksToSubstractOrAdd) throws JcalendarTimeConflictException {
        // head
        String[] weekNoHL;
        String[] weekHL;
        if (weeksToSubstractOrAdd == 0) {
            LocalDate date = model.getDate();
            List<LocalDate> wd = Jcalendar.getWholeWeek(date);
            weekNoHL = Jcalendar.convertDateFormatForOneWeek(wd);
            weekHL = Jcalendar.highlightToday(weekNoHL);
            for (int l = 0; l < 7; l++) {
                weekHL[l] = Jcalendar.resizeString(weekHL[l], 9, "left");
            }
            System.out.printf(headerFormat, weekHL[0], weekHL[1], weekHL[2], weekHL[3], weekHL[4], weekHL[5], weekHL[6],
                                            weekHL[7], weekHL[8], weekHL[9], weekHL[10], weekHL[11], weekHL[12], weekHL[13]);

        } else {
            String[] weekDays = Jcalendar.weekDays;
            weekNoHL = Jcalendar.getPastFutureWeeksDates(weeksToSubstractOrAdd);
            for (int l = 0; l < 7; l++) {
                weekDays[l] = Jcalendar.resizeString(weekDays[l], 9, "left");
            }
            System.out.printf(headerFormat, weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6],
                                            weekNoHL[0], weekNoHL[1], weekNoHL[2], weekNoHL[3], weekNoHL[4], weekNoHL[5], weekNoHL[6]);
        }

        // body
        String[][] tab = new String[24][7];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }
        for (int i = 0; i < 7; i++) {
            JcalendarModel day = new JcalendarModel(Jcalendar.stringToLocalDate(weekNoHL[i]));
            SortedSet<Activity> activities = day.getActivitiesInTheDay();
            if (activities != null) {
                for (Activity activity : activities) {
                    int indexX = Jcalendar.getTimeIntervalIndex(activity.startTime());
                    int indexY = i;
                    String content = activity.activityTitle();
                    tab[indexX][indexY] = content.length() > 14 ? content.substring(0, 11) + "..." : String.format("%" + (-14) + "s", content);
                }
            }
        }

        String[] flatTab = Arrays.stream(tab)
                                 .flatMap(Stream::of)
                                 .toArray(String[]::new);
        System.out.printf(bodyFormat, flatTab);
    }

    public static void main(String[] args) throws JcalendarTimeConflictException {
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("11:30"), Jcalendar.stringToLocalTime("12:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("14:00"), Jcalendar.stringToLocalTime("15:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("12:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("14:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("15:30"), Jcalendar.stringToLocalTime("16:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
        JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));

        ViewWeek w = new ViewWeek();

            w.displayWeekSchedule(new JcalendarModel(Jcalendar.stringToLocalDate("03/01/2023")), 0);
        }
}
