package be.technifutur.jcalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class JcalendarModel {
    private LocalDate date;
    private String day;
    static Map<LocalDate, SortedSet<Activity>> recordList = new HashMap<>();
    private SortedSet<Activity> activitiesInTheDay;

    public JcalendarModel(LocalDate date) throws JcalendarTimeConflictException {
        this.date = date;
        this.day = Jcalendar.getDayFromDate(date);
        this.activitiesInTheDay = getRecord(date).orElse(null);
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

    public SortedSet<Activity> getActivitiesInTheDay() {
        return activitiesInTheDay;
    }

    public static void addRecord(Activity activity) throws JcalendarTimeConflictException {
        if (isTimeConflict(activity.date(), activity.startTime())) {
            throw new JcalendarTimeConflictException("Les horaires entrés sont conflictuel !");
        } else {
            recordList.computeIfAbsent(activity.date(), k -> new TreeSet<>()).add(activity);
        }
    }

    public Optional<SortedSet<Activity>> getRecord(LocalDate date) {
        return Optional.ofNullable(recordList.get(date));
    }

    public Optional<SortedSet<Activity>> getRecord(String location) {
        SortedSet<Activity> result = new TreeSet<>();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.location().equals(location)) {
                    result.add(record);
                }
            }
        }
        return Optional.of(result);
    }

    public Optional<SortedSet<Activity>> getRecord(ActivityType type) {
        SortedSet<Activity> result = new TreeSet<>();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.type().equals(type)) {
                    result.add(record);
                }
            }
        }
        return Optional.of(result);
    }

    public void deleteRecord(Activity activity) {
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.equals(activity)) {
                    recordsInOneDate.remove(activity);
                }
            }
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

    public static boolean isTimeConflict(LocalDate date, LocalTime strTime) {
        if (recordList.containsKey(date)) {
            for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
                SortedSet<Activity> recordsInOneDate = entry.getValue();
                for (Activity record : recordsInOneDate) {
                    if (strTime.isAfter(record.startTime()) && strTime.isBefore(record.endTime().minusMinutes(1))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
