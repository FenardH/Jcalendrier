package be.technifutur.jcalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Person implements Serializable {
    private String nom;
    private String prenom;
    private Club club = Club.TECHNIFUTUR;
    private double tarif;
    private Map<LocalDate, SortedSet<Activity>> activityList = new HashMap<>();
    private Map<Activity, Boolean> presenceList = new HashMap<>();
    Jcalendar jcalendar = new Jcalendar();

    public Person(String prenom, String nom, Club club, double tarif) {
        this.prenom = prenom;
        this.nom = nom;
        this.club = club;
        this.tarif = tarif;
    }
    public Person(String prenom, String nom) {
        this(prenom, nom, Club.TECHNIFUTUR, 0);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {return prenom;}

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Optional<SortedSet<Activity>> getActivity(LocalDate date) throws JcalendarTimeConflictException {
        updateActivity();
        return Optional.ofNullable(activityList.get(date));
    }

    public Map<LocalDate, SortedSet<Activity>> getAllActivities() throws JcalendarTimeConflictException {
        updateActivity();
        return activityList;
    }

    public void addActivity(Activity activity) throws JcalendarTimeConflictException {
        if (isTimeConflict(activity.getDate(), activity.getStartTime())) {
            throw new JcalendarTimeConflictException(Text.red("horaires encodés sont conflictuels !"));
        } else {
            activityList.computeIfAbsent(activity.getDate(), k -> new TreeSet<>()).add(activity);
            presenceList.put(activity, false);
        }
    }

    public void deleteActivity(String d, String st, String et, String activityTitle, String location, String ate) throws JcalendarNoRecordException, JcalendarTimeConflictException {
        updateActivity();
        LocalDate date = jcalendar.stringToLocalDate(d);
        LocalTime startTime = jcalendar.stringToLocalTime(st);
        LocalTime endTime = jcalendar.stringToLocalTime(et);
        ActivityType activityType = ActivityType.valueOf(ate.toUpperCase());
        if (activityList.containsKey(date)) {
            SortedSet<Activity> recordsInOneDate = activityList.get(date);
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
                presenceList.remove(activityToDelete);
                if (recordsInOneDate.size() == 0) {
                    activityList.remove(date);
                }
            }else {
                throw new JcalendarNoRecordException("\nenregistrment n'existe pas !\n");
            }
        } else {
            throw new JcalendarNoRecordException("\nenregistrement n'existe pas !\n");
        }
    }

    public void setPresence(Activity activity, boolean isPresent) {
        presenceList.put(activity, isPresent);
    }

    public Map<Activity, Boolean> getPresenceList() {
        return presenceList;
    }

    private void updateActivity() throws JcalendarTimeConflictException {
        JcalendarModel model = new JcalendarModel();
        Map<LocalDate, SortedSet<Activity>> recordList = model.getAllRecords();
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : activityList.entrySet()) {
            LocalDate date = entry.getKey();
            if (recordList.containsKey(date)) {
                SortedSet<Activity> recordsInOneDate = entry.getValue();
                for (Activity activity : recordsInOneDate) {
                    if (!recordList.get(date).contains(activity)) {
                        activityList.get(date).remove(activity);
                    }
                }
            } else {
                activityList.remove(date);
            }
        }
    }

    public int getRecordsNumber(LocalDate date) throws JcalendarTimeConflictException {
        updateActivity();
        if (activityList.containsKey(date)) {
            return activityList.get(date).size();
        } else {
            return 0;
        }
    }

    public int getRecordsNumber() throws JcalendarTimeConflictException {
        return activityList.values().stream()
                                    .mapToInt(SortedSet::size)
                                    .sum();
    }

    public List<Activity> getAllRecordsAsList(boolean isPrint) {
        int count = 0;
        List<Activity> allRecords = new ArrayList<>();
        for (Map.Entry<Activity, Boolean> entry : presenceList.entrySet()) {
            Activity activity = entry.getKey();
            boolean isPresence = entry.getValue();
            if (isPrint) {
                System.out.printf("[%s] %s Présence: %s\n", count + 1, activity, isPresence ? "Oui" : "Non");
                count++;
            }
            allRecords.add((activity));
        }
        return allRecords;
    }

    public double getTotalFee() throws JcalendarTimeConflictException {
        updateActivity();
        double sum = 0;
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : activityList.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (Activity activity : recordsInOneDate) {
                sum += activity.tarif;
            }
        }
        return sum;
    }

    private boolean isTimeConflict(LocalDate date, LocalTime strTime) {
        if (activityList.containsKey(date)) {
            SortedSet<Activity> recordsInOneDate = activityList.get(date);
            for (Activity record : recordsInOneDate) {
                if (strTime.isAfter(record.getStartTime()) && strTime.isBefore(record.getEndTime().minusMinutes(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        if (!nom.equals(person.nom)) {
            return false;
        }
        if (!prenom.equals(person.prenom)) {
            return false;
        }
        return club == person.club;
    }

    @Override
    public int hashCode() {
        int result = nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + club.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return  "nom='" + nom + '\'' +
                ", prénom='" + prenom + '\'' +
                ", club=" + club +
                ", tarif=" + tarif +
                "\nactivités:\n" + activityList;
    }
}
