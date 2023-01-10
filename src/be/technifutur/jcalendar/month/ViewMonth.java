package be.technifutur.jcalendar.month;

import be.technifutur.jcalendar.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ViewMonth{
    private static final String headFormat = """
            Utilisateur: %s
            %s, %s
            +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
            +     .  +     .  +    .   +     .  +    .   +     .  +    .   +
            +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
            """.replaceAll("\\.","%s");

    private static final String bodyFormat = """
            | . | . | . | . | . | . | . |
            | . | . | . | . | . | . | . |
            +----------------+----------------+----------------+----------------+----------------+----------------+----------------+
            """.replaceAll("\\.","%s");

    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;

    public void displayMonthSchedule (JcalendarModel model, int monthsToSubstractOrAdd) throws JcalendarException {
        LocalDate date = model.minusOrPlusMonths(monthsToSubstractOrAdd);

        // head
        String[] weekDays = jcalendar.weekDays;
        for (int l = 0; l < 7; l++) {
            weekDays[l] = Text.resizeString(weekDays[l], 9, "left");
        }
        System.out.printf(headFormat, "admin",
                                      jcalendar.monthEnToFr(date.getMonth().toString()), date.getYear(),
                                      weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6]);

        // body
        List<LocalDate> monthDays = jcalendar.getWholeMonth(date);
        List<LocalDate> monthDaysExtended = jcalendar.getWholeMonthExtended(monthDays);

        int rowNum = monthDaysExtended.size() / 7;
        String[][] tab = new String[rowNum * 2][7];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }

        StringBuilder body = new StringBuilder();
        for (int i = 0; i < rowNum; i++) {
            body.append(bodyFormat);
        }

        LocalDate dateVal;
        String contentDate;
        String contentInfo;
        int activityNum;
        JcalendarModel dayModel;
        for (int j = 0; j < monthDaysExtended.size(); j++) {
            dateVal = monthDaysExtended.get(j);
            if (monthDays.contains(dateVal)) {
                dayModel = new JcalendarModel(dateVal);
                if (dateVal.equals(today)) {
                    contentDate = Text.yellow(jcalendar.localDateToString(dateVal));
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = dayModel.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? activityNum : " " + activityNum);
                        contentInfo = Text.yellow(contentInfo);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                } else {
                    contentDate = jcalendar.localDateToString(dateVal);
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = dayModel.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? activityNum : " " + activityNum);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                }

            } else {
                contentDate = Text.white(jcalendar.localDateToString(dateVal));
                tab[j/7*2][j%7] = "  " + contentDate + "  ";
            }
        }

        String[] flatTab = Arrays.stream(tab)
                .flatMap(Stream::of)
                .toArray(String[]::new);
        System.out.printf(body.toString(), flatTab);
    }

    public void displayMonthSchedule (Person person, int monthsToSubstractOrAdd) throws JcalendarException {
        LocalDate date = LocalDate.from(today);
        if (monthsToSubstractOrAdd > 0) {
            date = date.plusMonths(monthsToSubstractOrAdd);
        } else if (monthsToSubstractOrAdd < 0) {
            date = date.minusMonths(-monthsToSubstractOrAdd);
        }

        // head
        String[] weekDays = jcalendar.weekDays;
        for (int l = 0; l < 7; l++) {
            weekDays[l] = Text.resizeString(weekDays[l], 9, "left");
        }
        System.out.printf(headFormat, person.getPrenom() + " " + person.getNom(),
                                      jcalendar.monthEnToFr(date.getMonth().toString()), date.getYear(),
                                      weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6]);

        // body
        List<LocalDate> monthDays = jcalendar.getWholeMonth(date);
        List<LocalDate> monthDaysExtended = jcalendar.getWholeMonthExtended(monthDays);

        int rowNum = monthDaysExtended.size() / 7;
        String[][] tab = new String[rowNum * 2][7];
        for (String[] row : tab) {
            Arrays.fill(row, "              ");
        }

        StringBuilder body = new StringBuilder();
        for (int i = 0; i < rowNum; i++) {
            body.append(bodyFormat);
        }

        LocalDate dateVal;
        String contentDate;
        String contentInfo;
        int activityNum;
        for (int j = 0; j < monthDaysExtended.size(); j++) {
            dateVal = monthDaysExtended.get(j);
            if (monthDays.contains(dateVal)) {
                if (dateVal.equals(today)) {
                    contentDate = Text.yellow(jcalendar.localDateToString(dateVal));
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = person.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? activityNum : " " + activityNum);
                        contentInfo = Text.yellow(contentInfo);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                } else {
                    contentDate = jcalendar.localDateToString(dateVal);
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = person.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? activityNum : " " + activityNum);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                }

            } else {
                contentDate = Text.white(jcalendar.localDateToString(dateVal));
                tab[j/7*2][j%7] = "  " + contentDate + "  ";
            }
        }

        String[] flatTab = Arrays.stream(tab)
                .flatMap(Stream::of)
                .toArray(String[]::new);
        System.out.printf(body.toString(), flatTab);
    }
}
