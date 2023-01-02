package be.technifutur.jcalendar;

import javax.sql.rowset.serial.SerialArray;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public interface JcalendarModel {
    Map<String, Set<Activity>> recordList = new HashMap<>();

    public default void addRecord(Activity activity) {
        String key = activity.date();
        if (recordList.getOrDefault(key, Collections.emptySet()).contains(activity)) {
            recordList.get(key).add(activity);
        } else {
            recordList.computeIfAbsent(key, k -> new HashSet<>()).add(activity);
        }
    }

    public default Optional<Set<Activity>> getRecord(Person person) {
        Set<Activity> result = new HashSet<>();
        for (Map.Entry<String, Set<Activity>> entry : recordList.entrySet()) {
            Set<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.person().equals(person)) {
                    result.add(record);
                }
            }
        }
        return Optional.of(result);
    }

    public default Optional<Set<Activity>> getRecord(LocalDate date) {
        String d = JcalendarDateTime.convertLocalDateToString(date);
        if (recordList.containsKey(d)) {
            return Optional.ofNullable(recordList.get(d));
        }
        return null;
    }

    public default Optional<Set<Activity>> getRecord(String location) {
        Set<Activity> result = new HashSet<>();
        for (Map.Entry<String, Set<Activity>> entry : recordList.entrySet()) {
            Set<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.location().equals(location)) {
                    result.add(record);
                }
            }
        }
        return Optional.of(result);
    }

    public default Optional<Set<Activity>> getRecord(ActivityType type) {
        Set<Activity> result = new HashSet<>();
        for (Map.Entry<String, Set<Activity>> entry : recordList.entrySet()) {
            Set<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.type().equals(type)) {
                    result.add(record);
                }
            }
        }
        return Optional.of(result);
    }

    public default void deleteRecord(Activity activity) {
        for (Map.Entry<String, Set<Activity>> entry : recordList.entrySet()) {
            Set<Activity> recordsInOneDate = entry.getValue();
            for (Activity record : recordsInOneDate) {
                if (record.equals(activity)) {
                    recordsInOneDate.remove(activity);
                }
            }
        }
    }

    public default int getRecordsNumber(String date) {
        Set<Activity> records = recordList.get(date);
        return records.size();
    }

    public default Map<String, Set<Activity>> getAllRecords() {
        return recordList;
    }
}
