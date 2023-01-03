package be.technifutur.jcalendar;

import java.time.LocalDate;
import java.util.*;

public class JcalendarModel {
    private LocalDate date;
    private String day;
    static Map<LocalDate, SortedSet<Activity>> recordList = new HashMap<>();
    private SortedSet<Activity> activitiesInTheDay;

    public JcalendarModel(LocalDate date) throws JcalendarTimeConflictException {
        this.date = date;
        this.day = Jcalendar.getDayFromDate(date);
        Optional<SortedSet<Activity>> findActivities = getRecord(date);
        this.activitiesInTheDay = findActivities.orElse(null);
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

    public void addActivitiesInTheDay(Activity activity) throws JcalendarTimeConflictException {
        if (activity.date() != date) {
            throw new JcalendarTimeConflictException("La date entrée n'est pas valide!");
        } else {
            this.activitiesInTheDay.add(activity);
        }
    }

    public static void addRecord(Activity activity) {
        LocalDate key = activity.date();
        /*if (recordList.getOrDefault(key, Collections.emptySet()).contains(activity)) {
            recordList.get(key).add(activity);
        } else {*/
        recordList.computeIfAbsent(key, k -> new TreeSet<>()).add(activity);
        //}
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
        return records.size();
    }

    public Map<LocalDate, SortedSet<Activity>> getAllRecords() {
        return recordList;
    }

    //    public static void main(String[] args) {
//        JcalendarModel m = new ModelDay();
//
//        m.addRecord(new Activity("30/12/2022", "09:00", "10:00", new Person("Johnson", "Boris"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        m.addRecord(new Activity("30/12/2022", "10:30", "11:30", new Person("Johnson", "Boris"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//        m.addRecord(new Activity("31/12/2022", "09:00", "10:00", new Person("Henry", "Thierry"), "Java Course", "Technifutur", ActivityType.SEANCE, true));
//        m.addRecord(new Activity("31/12/2022", "09:30", "10:30", new Person("Harry", "kane"), "JavaScript Course", "Technifutur", ActivityType.SEANCE, true));
//        m.addRecord(new Activity("31/12/2022", "10:30", "11:30", new Person("Henry", "Thierry"), "Java Course", "Technipaste", ActivityType.REPOS, true));
//        LocalDate date = JcalendarDateTime.convertStringToLocalDate("30/12/2022");
//        System.out.println(m.getRecord(date));
//        System.out.println("------------");
//        System.out.println(m.getRecord(ActivityType.SEANCE));
//        System.out.println("------------");
//        Person p = new Person("Johnson", "Boris");
//        System.out.println(m.getRecord(p));
//        System.out.println("------------");
//        System.out.println(m.getRecord("Technifutur"));
//        System.out.println("------------");
//        System.out.println(m.getRecordsNumber("31/12/2022"));
//        System.out.println("------------");
//        System.out.println(m.getAllRecords());
//        System.out.println("------------");
//    }
}
