package be.technifutur.jcalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static be.technifutur.jcalendar.Jcalendar.today;

public class JcalendarModel {
    private LocalDate date;
    private String day;
    static Map<LocalDate, SortedSet<Activity>> recordList = new HashMap<>();

    public JcalendarModel(LocalDate date) throws JcalendarTimeConflictException {
        this.date = date;
        this.day = Jcalendar.getDayFromDate(date);
    }

    public JcalendarModel() throws JcalendarTimeConflictException {
        this(today);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void addRecord(Activity activity) throws JcalendarTimeConflictException {
        if (isTimeConflict(activity.getDate(), activity.getStartTime())) {
            throw new JcalendarTimeConflictException("Les horaires entrés sont conflictuel !");
        } else {
            recordList.computeIfAbsent(activity.getDate(), k -> new TreeSet<>()).add(activity);
        }
    }

    public static Optional<SortedSet<Activity>> getRecord(LocalDate date) {
        return Optional.ofNullable(recordList.get(date));
    }

    public Optional<SortedSet<Activity>> getRecord(String location) {
        SortedSet<Activity> result = new TreeSet<>();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.getLocation().equals(location)) {
                    result.add(record);
                }
            }
        }
        return Optional.ofNullable(result);
    }

    public Optional<SortedSet<Activity>> getRecord(ActivityType type) {
        SortedSet<Activity> result = new TreeSet<>();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.getType().equals(type)) {
                    result.add(record);
                }
            }
        }
        return Optional.ofNullable(result);
    }

    public Optional<SortedSet<Activity>> getActivitiesInTheDay() {
        return getRecord(this.date);
    }

    public void deleteRecord(Activity activity) throws JcalendarNoRecordException {
        LocalDate date = activity.getDate();
        if (recordList.containsKey(date)) {
            SortedSet<Activity> recordsInOneDate = recordList.get(date);
            recordsInOneDate.remove(activity);
            if (recordsInOneDate.size() == 0) {
                recordList.remove(date);
            }
        } else {
            throw new JcalendarNoRecordException("Record n'existe pas !");
        }
    }

    public int getRecordsNumber(LocalDate date) {
        SortedSet<Activity> records = recordList.get(date);
        if (records != null) {
            return records.size();
        } else {
            return 0;
        }
    }

    public Map<LocalDate, SortedSet<Activity>> getAllRecords() {
        return recordList;
    }

    private boolean isTimeConflict(LocalDate date, LocalTime strTime) {
        if (recordList.containsKey(date)) {
            SortedSet<Activity> recordsInOneDate = recordList.get(date);
            for (Activity record : recordsInOneDate) {
                if (strTime.isAfter(record.getStartTime()) && strTime.isBefore(record.getEndTime().minusMinutes(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void minusOrPlusDays(int dayNbr) throws JcalendarTimeConflictException {
        if (dayNbr < 0) {
            date = date.minusDays(-dayNbr);
            day = Jcalendar.getDayFromDate(date);
        } else if (dayNbr > 0) {
            date = date.plusDays(dayNbr);
            day = Jcalendar.getDayFromDate(date);
        }
    }

        /*public static void main(String[] args) throws JcalendarTimeConflictException {
            JcalendarModel m = new JcalendarModel(Jcalendar.stringToLocalDate("30/12/2022"));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:00"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("14:00"), Jcalendar.stringToLocalTime("15:00"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("10:00"), Jcalendar.stringToLocalTime("10:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
            JcalendarModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS, true));

            System.out.println(JcalendarModel.isTimeConflict(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:30")));
            System.out.println("------------");
            System.out.println(m.getRecord(ActivityType.SEANCE));
            System.out.println("------------");
            System.out.println(m.getRecord("Technifutur"));
            System.out.println("------------");
            System.out.println(m.getRecordsNumber(Jcalendar.stringToLocalDate("31/12/2022")));
            System.out.println("------------");
            System.out.println(m.getAllRecords());
            System.out.println("------------");
    }*/
}
