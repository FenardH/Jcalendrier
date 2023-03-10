package be.technifutur.jcalendar.week;

import be.technifutur.jcalendar.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class ViewWeek {
    private final String headerFormat = """
                Utilisateur: %s
                      +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
                      +     .  +     .  +    .   +     .  +    .   +     .  +    .   +
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

    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;

    public void displayWeekSchedule (JcalendarModel model, int weeksToSubstractOrAdd) throws JcalendarException {
        // head
        String[] weekNoHL;
        String[] weekHL;
        if (weeksToSubstractOrAdd == 0) {
            LocalDate date = model.getDate();
            List<LocalDate> wd = jcalendar.getWholeWeek(date);
            weekNoHL = jcalendar.convertDateFormatForOneWeek(wd);
            weekHL = jcalendar.highlightToday(weekNoHL);
            for (int l = 0; l < 7; l++) {
                Pattern pattern = Pattern.compile("\u001B\\[0m");
                Matcher matcher = pattern.matcher(weekHL[l+7]);
                boolean matchFound = matcher.find();
                if (l == jcalendar.getWeekDayIndex(today) && matchFound) {
                    weekHL[l] = Text.resizeString(weekHL[l], 18, "left");
                } else {
                    weekHL[l] = Text.resizeString(weekHL[l], 9, "left");
                }
            }
            System.out.printf(headerFormat, "admin",
                                            weekHL[0], weekHL[1], weekHL[2], weekHL[3], weekHL[4], weekHL[5], weekHL[6],
                                            weekHL[7], weekHL[8], weekHL[9], weekHL[10], weekHL[11], weekHL[12], weekHL[13]);
        } else {
            String[] weekDays = jcalendar.weekDays;
            weekNoHL = jcalendar.getPastFutureWeeksDates(weeksToSubstractOrAdd);
            for (int l = 0; l < 7; l++) {
                weekDays[l] = Text.resizeString(weekDays[l], 9, "left");
            }
            System.out.printf(headerFormat, "admin",
                                            weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6],
                                            weekNoHL[0], weekNoHL[1], weekNoHL[2], weekNoHL[3], weekNoHL[4], weekNoHL[5], weekNoHL[6]);
        }

        // body
        String[][] tab = new String[24][7];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }
        for (int y = 0; y < 7; y++) {
            JcalendarModel day = new JcalendarModel(jcalendar.stringToLocalDate(weekNoHL[y]));
            Optional<SortedSet<Activity>> activities = day.getActivitiesInTheDay();
            if (activities.isPresent()) {
                for (Activity activity : activities.get()) {
                    int x = jcalendar.getTimeIntervalIndex(activity.getStartTime());
                    String content = activity.getActivityTitle();
                    tab[x][y] = content.length() > 14 ? content.substring(0, 11) + "..." : String.format("%" + (-14) + "s", content);
                }
            }
        }
        String[] flatTab = Arrays.stream(tab)
                                 .flatMap(Stream::of)
                                 .toArray(String[]::new);
        System.out.printf(bodyFormat, flatTab);
    }

    public void displayWeekSchedule (Person person, int weeksToSubstractOrAdd) throws JcalendarException {
        // head
        String[] weekNoHL;
        String[] weekHL;
        if (weeksToSubstractOrAdd == 0) {
            LocalDate date = LocalDate.from(today);
            List<LocalDate> wd = jcalendar.getWholeWeek(date);
            weekNoHL = jcalendar.convertDateFormatForOneWeek(wd);
            weekHL = jcalendar.highlightToday(weekNoHL);
            for (int l = 0; l < 7; l++) {
                Pattern pattern = Pattern.compile("\u001B\\[0m");
                Matcher matcher = pattern.matcher(weekHL[l+7]);
                boolean matchFound = matcher.find();
                if (l == jcalendar.getWeekDayIndex(today) && matchFound) {
                    weekHL[l] = Text.resizeString(weekHL[l], 18, "left");
                } else {
                    weekHL[l] = Text.resizeString(weekHL[l], 9, "left");
                }
            }
            System.out.printf(headerFormat, person.getPrenom() + " " + person.getNom(),
                                            weekHL[0], weekHL[1], weekHL[2], weekHL[3], weekHL[4], weekHL[5], weekHL[6],
                                            weekHL[7], weekHL[8], weekHL[9], weekHL[10], weekHL[11], weekHL[12], weekHL[13]);
        } else {
            String[] weekDays = jcalendar.weekDays;
            weekNoHL = jcalendar.getPastFutureWeeksDates(weeksToSubstractOrAdd);
            for (int l = 0; l < 7; l++) {
                weekDays[l] = Text.resizeString(weekDays[l], 9, "left");
            }
            System.out.printf(headerFormat, person.getPrenom() + " " + person.getNom(),
                                            weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6],
                                            weekNoHL[0], weekNoHL[1], weekNoHL[2], weekNoHL[3], weekNoHL[4], weekNoHL[5], weekNoHL[6]);
        }

        // body
        String[][] tab = new String[24][7];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }
        for (int y = 0; y < 7; y++) {
            Optional<SortedSet<Activity>> activities = person.getActivity(jcalendar.stringToLocalDate(weekNoHL[y]));
            if (activities.isPresent()) {
                for (Activity activity : activities.get()) {
                    int x = jcalendar.getTimeIntervalIndex(activity.getStartTime());
                    String content = activity.getActivityTitle();
                    tab[x][y] = content.length() > 14 ? content.substring(0, 11) + "..." : String.format("%" + (-14) + "s", content);
                }
            }
        }
        String[] flatTab = Arrays.stream(tab)
                                 .flatMap(Stream::of)
                                 .toArray(String[]::new);
        System.out.printf(bodyFormat, flatTab);
    }
}
