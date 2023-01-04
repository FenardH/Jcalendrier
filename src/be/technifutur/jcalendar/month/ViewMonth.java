package be.technifutur.jcalendar.month;

import be.technifutur.jcalendar.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static be.technifutur.jcalendar.Jcalendar.*;

public class ViewMonth{
    private static final String headFormat = """
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

    public void displayMonthSchedule (JcalendarModel model, int monthsToSubstractOrAdd) throws JcalendarException {
        LocalDate date;
        if (monthsToSubstractOrAdd > 0) {
            date = model.getDate().plusMonths(monthsToSubstractOrAdd);
        } else if (monthsToSubstractOrAdd < 0) {
            date = model.getDate().minusMonths(-monthsToSubstractOrAdd);
        } else {
            date = model.getDate();
        }

        // head
        String[] weekDays = Jcalendar.weekDays;
        for (int l = 0; l < 7; l++) {
            weekDays[l] = Jcalendar.resizeString(weekDays[l], 9, "left");
        }
        System.out.printf(headFormat, date.getMonth(), date.getYear(),
                                      weekDays[0], weekDays[1], weekDays[2], weekDays[3], weekDays[4], weekDays[5], weekDays[6]);

        // body
        List<LocalDate> monthDays = getWholeMonth(date);
        List<LocalDate> monthDaysExtended = getWholeMonthExtended(monthDays);

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
                if (dayModel.getDate().equals(today)) {
                    contentDate = TextColor.yellow(localDateToString(dateVal));
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = dayModel.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? " " + activityNum : activityNum);
                        contentInfo = TextColor.yellow(contentInfo);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                } else {
                    contentDate = localDateToString(dateVal);
                    tab[j/7*2][j%7] = "  " + contentDate + "  ";
                    activityNum = dayModel.getRecordsNumber(dateVal);
                    if (activityNum != 0) {
                        contentInfo = String.format("%s activité(s)", activityNum > 9 ? activityNum : " " + activityNum);
                        tab[j/7*2+1][j%7] = contentInfo;
                    }
                }

            } else {
                contentDate = TextColor.white(localDateToString(dateVal));
                tab[j/7*2][j%7] = "  " + contentDate + "  ";
            }
        }

        String[] flatTab = Arrays.stream(tab)
                .flatMap(Stream::of)
                .toArray(String[]::new);
        System.out.printf(body.toString(), flatTab);
    }
    public static void main(String[] args) throws JcalendarException {
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

        ViewMonth m = new ViewMonth();

            m.displayMonthSchedule(new JcalendarModel(Jcalendar.stringToLocalDate("03/01/2023")), 0);
    }
}
