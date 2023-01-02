package be.technifutur.jcalendar;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class JcalendarDateTime {
    static final LocalDate today = LocalDate.now();
    static final String todayInString = convertLocalDateToString(today);
    static final String[] weekDays = {"-Lundi-", "-Mardi-", "-Mercredi-", "-Jeudi-", "-Vendredi-", "-Samedi-", "-Dimanche-"};

    public static String convertLocalDateToString(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE).format(date);
    }

    public static LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);
        return LocalDate.parse(date, formatter);
    }

    static List<LocalDate> wd = getWholeWeek(today);
    public static String[] currentWeekDays = convertDateFormatForOneWeek(wd);
    public static String[] currentWeekDaysAndDatesHL = highlightToday(currentWeekDays);

//        LocalDate d1 = today.minus(1, ChronoUnit.DAYS);

    private static String[] convertDateFormatForOneWeek(List<LocalDate> week) {
        String[] weekDaysString = new String[week.size()];
        for (int i = 0; i < week.size(); i++) {
            weekDaysString[i] = convertLocalDateToString(week.get(i));
        }
        return weekDaysString;
    }

    private static String[] highlightToday(String[] week) {
        String[] newWeekDays = new String[week.length];

        for (int i = 0; i < week.length; i++) {
            if (week[i].equals(todayInString)) {
                currentWeekDays[i] = TextColor.yellow(currentWeekDays[i]);
                week[i] = TextColor.yellow(week[i]);
            }
            newWeekDays[i] = week[i];
        }

        return Stream.concat(Arrays.stream(currentWeekDays), Arrays.stream(newWeekDays))
                                                                   .toArray(size -> (String[]) Array
                                                                   .newInstance(currentWeekDays.getClass().getComponentType(), size));
    }

    public static String[] getPastFutureWeeksDates(int weekNbr) {
        if (weekNbr < 0) {
            LocalDate dayInPrevousWeek = today.minusWeeks(0 - weekNbr);
            List<LocalDate> prevousWeek = getWholeWeek(dayInPrevousWeek);
            return convertDateFormatForOneWeek(prevousWeek);
        } else if (weekNbr > 0) {
            LocalDate dayInPrevousWeek = today.plusWeeks(weekNbr);
            List<LocalDate> prevousWeek = getWholeWeek(dayInPrevousWeek);
            return convertDateFormatForOneWeek(prevousWeek);
        } else {
            return currentWeekDays;
        }
    }

    private static List<LocalDate> getWholeWeek(LocalDate day) {
        return Arrays.asList(DayOfWeek.values()).stream()
                                                .map(day::with)
                                                .collect(toList());
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getPastFutureWeeksDates(-2)));
        System.out.println(Arrays.toString(getPastFutureWeeksDates(-1)));
        System.out.println(Arrays.toString(currentWeekDays));
        System.out.println(Arrays.toString(getPastFutureWeeksDates(1)));
        System.out.println(Arrays.toString(getPastFutureWeeksDates(2)));

    }
}
