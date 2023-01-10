package be.technifutur.jcalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class JcalendarModel implements Serializable {
    private LocalDate date;
    private String day;
    private static Map<LocalDate, SortedSet<Activity>> recordList = new HashMap<>();

    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;

    public JcalendarModel(LocalDate date) throws JcalendarTimeConflictException {
        this.date = date;
        this.day = jcalendar.getDayFromDate(date);
    }

    public JcalendarModel() throws JcalendarTimeConflictException {
        this(new Jcalendar().today);
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
            throw new JcalendarTimeConflictException(Text.red("horaires encodés sont conflictuels !"));
        } else {
            recordList.computeIfAbsent(activity.getDate(), k -> new TreeSet<>()).add(activity);
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

    public void deleteRecord(String d, String st, String et, String activityTitle, String location, String ate) throws JcalendarNoRecordException {
        LocalDate date = jcalendar.stringToLocalDate(d);
        LocalTime startTime = jcalendar.stringToLocalTime(st);
        LocalTime endTime = jcalendar.stringToLocalTime(et);
        ActivityType activityType = ActivityType.valueOf(ate.toUpperCase());
        if (recordList.containsKey(date)) {
            SortedSet<Activity> recordsInOneDate = recordList.get(date);
            Activity activityToDelete = null;
            for (Activity activity : recordsInOneDate) {
                if (activity.startTime.equals(startTime) &&
                    activity.endTime.equals(endTime) &&
                    activity.activityTitle.equals(activityTitle) &&
                    activity.location.equals(location) &&
                    activity.type.equals(activityType)) {
                    activityToDelete = activity;
                }
            }
            if (activityToDelete != null) {
                recordsInOneDate.remove(activityToDelete);
                if (recordsInOneDate.size() == 0) {
                    recordList.remove(date);
                }
            }else {
                throw new JcalendarNoRecordException(Text.red("\nenregistrement n'existe pas !\n"));
            }
        } else {
            throw new JcalendarNoRecordException(Text.red("\nenregistrement n'existe pas !\n"));
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

    public int getRecordsNumber() {
        return recordList.values().stream()
                                  .mapToInt(SortedSet::size)
                                  .sum();
    }

    public Map<LocalDate, SortedSet<Activity>> getAllRecords() {
        return recordList;
    }

    public void setRecordList(Map<LocalDate, SortedSet<Activity>> activityList) {
        recordList = activityList;
    }

    public List<Activity> getAllRecordsAsList(boolean isPrint) {
        int count = 0;
        List<Activity> allRecords = new ArrayList<>();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : recordList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (int i = 0; i < recordsInOneDate.size(); i++) {
                if (isPrint) {
                    System.out.printf("[%s] %s\n", count + 1, recordsInOneDate.toArray()[i]);
                    count++;
                }
                allRecords.add((Activity) recordsInOneDate.toArray()[i]);
            }
        }
        return allRecords;
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

    public LocalDate minusOrPlusDays(int days) {
        LocalDate date = LocalDate.from(today);
        if (days > 0) {
            date = date.plusDays(days);
        } else if (days < 0) {
            date = date.minusDays(-days);
        }
        return date;
    }

    public LocalDate minusOrPlusWeeks(int weeks) {
        LocalDate date = LocalDate.from(today);
        if (weeks > 0) {
            date = date.plusWeeks(weeks);
        } else if (weeks < 0) {
            date = date.minusWeeks(-weeks);
        }
        return date;
    }

    public LocalDate minusOrPlusMonths(int months) {
        LocalDate date = LocalDate.from(today);
        if (months > 0) {
            date = date.plusMonths(months);
        } else if (months < 0) {
            date = date.minusMonths(-months);
        }
        return date;
    }
}
