package be.technifutur.jcalendar.month;

import be.technifutur.jcalendar.TextColor;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ViewMonth{
    private static final String headFormat = """
            %s, %s
            +-------------+-------------+-------------+-------------+-------------+-------------+-------------+
            +   %s   +   %s   + %s  +   %s   + %s  +  %s   + %s  +
            +-------------+-------------+-------------+-------------+-------------+-------------+-------------+
            """;

    private static final String bodyDayFormat = """
            |       .     |       .     |       .     |       .     |       .     |       .     |       .     |
            """.replaceAll("\\.","%s");

    private static final String bodyDayInfoFormat = """
            | . | . | . | . | . | . | . |
            +-------------+-------------+-------------+-------------+-------------+-------------+-------------+
            """.replaceAll("\\.","%s");

    public static void main(String[] args) {

        LocalDate today = LocalDate.now();
        List<LocalDate> w = Arrays.asList(DayOfWeek.values()).stream()
                .map(today::with)
                .collect(toList());

        String[] week = convertDateFormat(w);
        String[] weekHightlighted = highlightToday(week);

//        LocalDate d1 = today.minus(1, ChronoUnit.DAYS);

        String blankSpaces = "           ";

        System.out.println(String.format(headFormat,
                "Decembre", "2022",
                weekHightlighted[0], weekHightlighted[1], weekHightlighted[2], weekHightlighted[3], weekHightlighted[4], weekHightlighted[5], weekHightlighted[6]) +
                String.format(bodyDayFormat, 1, 2, 3, 4, 5, 6, 7) +
                String.format(bodyDayInfoFormat, blankSpaces, blankSpaces, blankSpaces, blankSpaces, blankSpaces, blankSpaces, blankSpaces));
    }

    private static String[] highlightToday(String[] week) {
        String[] days = {"-Lundi-", "-Mardi-", "-Mercredi-", "-Jeudi-", "-Vendredi-", "-Samedi-", "-Dimanche-"};
        String[] newWeek = new String[7];
        LocalDate td = LocalDate.now();
        String today = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE).format(td);

        for (int i = 0; i < week.length; i++) {
            if (week[i].equals(today)) {
                days[i] = TextColor.yellow(days[i]);
                week[i] = TextColor.yellow(week[i]);
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
