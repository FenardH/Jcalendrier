package be.technifutur.jcalendar.day;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ViewDay {
    private static final String headerFormat = """
            +-------+
            +   %s   +
            + %s  +
            +-------+---+
            """;



    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        List<LocalDate> w = Arrays.asList(DayOfWeek.values()).stream()
                                                             .map(today::with)
                                                             .collect(toList());

        String[] week = convertDateFormat(w);
        String[] weekHightlighted = highlightToday(week);

//        LocalDate d1 = today.minus(1, ChronoUnit.DAYS);

        System.out.println(String.format(headerFormat,
                weekHightlighted[0], weekHightlighted[1], weekHightlighted[2], weekHightlighted[3], weekHightlighted[4], weekHightlighted[5], weekHightlighted[6],
                weekHightlighted[7], weekHightlighted[8], weekHightlighted[9], weekHightlighted[10], weekHightlighted[11], weekHightlighted[12], weekHightlighted[13]));


    }

    private static String[] highlightToday(String[] week) {
        String[] newWeek = new String[7];
        String[] days = {"-Lundi-", "-Mardi-", "-Mercredi-", "-Jeudi-", "-Vendredi-", "-Samedi-", "-Dimanche-"};
        LocalDate td = LocalDate.now();
        String today = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE).format(td);
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RESET = "\u001B[0m";

        for (int i = 0; i < week.length; i++) {
            if (week[i].equals(today)) {
                days[i] = ANSI_YELLOW + days[i] + ANSI_RESET;
                week[i] = ANSI_YELLOW + week[i] + ANSI_RESET;
            }
            newWeek[i] = week[i];
        }

        return Stream.concat(Arrays.stream(days), Arrays.stream(newWeek))
                     .toArray(size -> (String[]) Array
                     .newInstance(days.getClass().getComponentType(), size));
    }

    private static String[] convertDateFormat(List<LocalDate> w) {
        String[] convertedDate = new String[7];
        for (int i = 0; i < w.size(); i++) {
            convertedDate[i] = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE).format(w.get(i));
        }
        return convertedDate;
    }
}
