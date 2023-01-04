package be.technifutur.jcalendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Jcalendar {
    public static final LocalDate today = LocalDate.now();
    public static final String todayInString = localDateToString(today);
    public static final String[] weekDays = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
    public static final String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                                           "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
    public static List<LocalDate> wd = getWholeWeek(today);
    public static String[] currentWeekDays = convertDateFormatForOneWeek(wd);

    public static List<LocalDate> getWholeWeek(LocalDate day) {
        return Arrays.stream(DayOfWeek.values())
                     .map(day::with)
                     .collect(toList());
    }

    public static List<LocalDate> getWholeMonth(LocalDate day) {
        List<LocalDate> monthDays = new ArrayList<>();

        LocalDate date = day.withDayOfMonth(1);
        LocalDate end = date.plusMonths(1);

        while (date.isBefore(end)) {
            monthDays.add(date);
            date = date.plusDays(1);
        }

        return monthDays;
    }

    public static List<LocalDate> getWholeMonthExtended(List<LocalDate> monthDays) throws JcalendarException {
        List<LocalDate> monthDaysExtended = new ArrayList<>(monthDays);

        LocalDate firstDate = monthDaysExtended.get(0);
        LocalDate lastDate = monthDaysExtended.get(monthDaysExtended.size() - 1);
        LocalDate tempDate;

        int daysBefore = getWeekDayIndex(firstDate);
        int daysBehind = 6 - getWeekDayIndex(lastDate);

        if (daysBefore != 0) {
            tempDate = firstDate;
            for (int i = 0; i < daysBefore; i++) {
                tempDate = tempDate.minusDays(1);
                monthDaysExtended.add(0, tempDate);
            }
        }

        if (daysBehind != 0) {
            tempDate = lastDate;
            for (int j = 0; j < daysBehind; j++) {
                tempDate = tempDate.plusDays(1);
                monthDaysExtended.add(tempDate);
            }
        }

        return monthDaysExtended;
    }

    public static String[] convertDateFormatForOneWeek(List<LocalDate> week) {
        String[] weekDaysString = new String[week.size()];
        for (int i = 0; i < week.size(); i++) {
            weekDaysString[i] = localDateToString(week.get(i));
        }
        return weekDaysString;
    }

    public static String[] highlightToday(String[] week) {
        String[] days = Arrays.copyOf(weekDays, 7);
        String[] newWeek = Arrays.copyOf(week, 7);
        for (int i = 0; i < newWeek.length; i++) {
            if (newWeek[i].equals(todayInString)) {
                days[i] = TextColor.yellow(days[i]);
                newWeek[i] = TextColor.yellow(newWeek[i]);
            }
        }
        return Stream.concat(Arrays.stream(days), Arrays.stream(newWeek)).toArray(String[]::new);
    }

    public static String localTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static LocalTime stringToLocalTime(String time) {
        int[] t = Arrays.stream(time.split(":")).mapToInt(Integer::parseInt).toArray();
        return LocalTime.of(t[0], t[1]);
    }

    public static String localDateToString(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE).format(date);
    }

    public static LocalDate stringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);
        return LocalDate.parse(date, formatter);
    }

    public static String[] getPastFutureWeeksDates(int weekNbr) {
        if (weekNbr < 0) {
            LocalDate dayInPrevousWeek = today.minusWeeks(-weekNbr);
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

    public static String getDayFromDate(String date) throws JcalendarTimeConflictException {
        String dayEn = String.valueOf(stringToLocalDate(date).getDayOfWeek());
        return weekDayEnToFr(dayEn);
    }

    public static String getDayFromDate(LocalDate date) throws JcalendarTimeConflictException {
        String dayEn = String.valueOf(date.getDayOfWeek());
        return weekDayEnToFr(dayEn);
    }

    public static String weekDayEnToFr(String dayEn) throws JcalendarTimeConflictException {
        return switch (dayEn) {
            case "MONDAY" -> weekDays[0];
            case "TUESDAY" -> weekDays[1];
            case "WEDNESDAY" -> weekDays[2];
            case "THURSDAY" -> weekDays[3];
            case "FRIDAY" -> weekDays[4];
            case "SATURDAY" -> weekDays[5];
            case "SUNDAY" -> weekDays[6];
            default -> throw new JcalendarTimeConflictException("Jour invalid !");
        };
    }

    public static String monthEnToFr(String dayEn) throws JcalendarTimeConflictException {
        return switch (dayEn) {
            case "JANUARY" -> months[0];
            case "FEBRUARY" -> months[1];
            case "MARCH" -> months[2];
            case "APRIL" -> months[3];
            case "MAY" -> months[4];
            case "JUNE" -> months[5];
            case "JULY" -> months[6];
            case "AUGUST" -> months[7];
            case "SEPTEMBER" -> months[8];
            case "OCTOBER" -> months[9];
            case "NOVEMBER" -> months[10];
            case "DECEMBER" -> months[11];
            default -> throw new JcalendarTimeConflictException("Mois invalid !");
        };
    }

    public static int getWeekDayIndex(LocalDate date) throws JcalendarException {
        return switch (String.valueOf(date.getDayOfWeek())) {
            case "MONDAY" -> 0;
            case "TUESDAY" -> 1;
            case "WEDNESDAY" -> 2;
            case "THURSDAY" -> 3;
            case "FRIDAY" -> 4;
            case "SATURDAY" -> 5;
            case "SUNDAY" -> 6;
            default -> throw new JcalendarException("Jour invalid !");
        };
    }

    public static int getTimeIntervalIndex(LocalTime aTime) {
        if (isInTimeInterval(aTime, 6, 7)) {
            return 0;
        } else if (isInTimeInterval(aTime, 7, 8)) {
            return 1;
        } else if (isInTimeInterval(aTime, 8, 9)) {
            return 2;
        } else if (isInTimeInterval(aTime, 9, 10)) {
            return 3;
        } else if (isInTimeInterval(aTime, 10, 11)) {
            return 4;
        } else if (isInTimeInterval(aTime, 11, 12)) {
            return 5;
        } else if (isInTimeInterval(aTime, 12, 13)) {
            return 6;
        } else if (isInTimeInterval(aTime, 13, 14)) {
            return 7;
        } else if (isInTimeInterval(aTime, 14, 15)) {
            return 8;
        } else if (isInTimeInterval(aTime, 15, 16)) {
            return 9;
        } else if (isInTimeInterval(aTime, 16, 17)) {
            return 10;
        } else if (isInTimeInterval(aTime, 17, 18)) {
            return 11;
        } else if (isInTimeInterval(aTime, 18, 19)) {
            return 12;
        } else if (isInTimeInterval(aTime, 19, 20)) {
            return 13;
        } else if (isInTimeInterval(aTime, 20, 21)) {
            return 14;
        } else if (isInTimeInterval(aTime, 21, 22)) {
            return 15;
        } else if (isInTimeInterval(aTime, 22, 23)) {
            return 16;
        } else if (isInTimeInterval(aTime, 23, 24)) {
            return 17;
        } else if (isInTimeInterval(aTime, 24, 0)) {
            return 18;
        } else if (isInTimeInterval(aTime, 0, 1)) {
            return 19;
        } else if (isInTimeInterval(aTime, 1, 2)) {
            return 20;
        } else if (isInTimeInterval(aTime, 2, 3)) {
            return 21;
        } else if (isInTimeInterval(aTime, 3, 4)) {
            return 22;
        } else if (isInTimeInterval(aTime, 4, 5)) {
            return 23;
        } else {
            return 24;
        }
    }

    private static boolean isInTimeInterval(LocalTime aTime, int startHour, int endHour) {
        return (aTime.isAfter(LocalTime.of(startHour - 1, 59)) && aTime.isBefore(LocalTime.of(endHour, 0)));
    }

    public static String resizeString(String s, int len, String padding) {
        if (padding.equalsIgnoreCase("left")) {
            return String.format("%" + (-len) + "s", s);
        } else if (padding.equalsIgnoreCase("right")) {
            return String.format("%" + len + "s", s);
        } else {
            return null;
        }
    }
//
//    public static void main(String[] args) throws JcalendarException {
//        List<LocalDate> m  = getWholeMonth(today.minusDays(1));
//        List<LocalDate> mex = getWholeMonthExtended(m);
//        System.out.println(m.size());
//        System.out.println(m);
//        System.out.println(mex.size());
//        System.out.println(mex);
//    }
}
