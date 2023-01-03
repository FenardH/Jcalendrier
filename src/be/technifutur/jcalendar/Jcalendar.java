package be.technifutur.jcalendar;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Jcalendar {
    static final LocalDate today = LocalDate.now();
    static final String todayInString = localDateToString(today);
    static final String[] weekDays = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};


    static List<LocalDate> wd = getWholeWeek(today);
    public static String[] currentWeekDays = convertDateFormatForOneWeek(wd);
    public static String[] currentWeekDaysAndDatesHL = highlightToday(currentWeekDays);

//        LocalDate d1 = today.minus(1, ChronoUnit.DAYS);

    public static List<LocalDate> getWholeWeek(LocalDate day) {
        return Arrays.asList(DayOfWeek.values()).stream()
                                                .map(day::with)
                                                .collect(toList());
    }

    public static String[] convertDateFormatForOneWeek(List<LocalDate> week) {
        String[] weekDaysString = new String[week.size()];
        for (int i = 0; i < week.size(); i++) {
            weekDaysString[i] = localDateToString(week.get(i));
        }
        return weekDaysString;
    }

    public static String[] highlightToday(String[] week) {
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

    public static String localTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static LocalTime stringToLocalTime(String time) {
        int[] t = Arrays.stream(time.split(":")).mapToInt(num -> Integer.parseInt(num)).toArray();
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

    public static String getDayFromDate(String date) throws JcalendarTimeConflictException {
        String dayEn = String.valueOf(stringToLocalDate(date).getDayOfWeek());
        return weekDayEnToFr(dayEn);
    }

    public static String getDayFromDate(LocalDate date) throws JcalendarTimeConflictException {
        String dayEn = String.valueOf(date.getDayOfWeek());
        return weekDayEnToFr(dayEn);
    }

    private static String weekDayEnToFr(String dayEn) throws JcalendarTimeConflictException {
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

    public int getWeekDayIndex(LocalDate date) throws JcalendarTimeConflictException {
        return switch (String.valueOf(date.getDayOfWeek())) {
            case "MONDAY" -> 0;
            case "TUESDAY" -> 1;
            case "WEDNESDAY" -> 2;
            case "THURSDAY" -> 3;
            case "FRIDAY" -> 4;
            case "SATURDAY" -> 5;
            case "SUNDAY" -> 6;
            default -> throw new JcalendarTimeConflictException("Jour invalid !");
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
        return (aTime.isAfter(LocalTime.of(startHour, 0)) && aTime.isBefore(LocalTime.of(endHour, 59)));
    }
}
