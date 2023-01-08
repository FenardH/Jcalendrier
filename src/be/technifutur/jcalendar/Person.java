package be.technifutur.jcalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Person {
    private String nom;
    private String prenom;
    private Club club = Club.TECHNIFUTUR;
    private double tarif;
    private Map<LocalDate, SortedSet<Activity>> activityList = new HashMap<>();

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

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public Optional<SortedSet<Activity>> getActivity(LocalDate date) {
        updateActivity();
        return Optional.ofNullable(activityList.get(date));
    }

    public Map<LocalDate, SortedSet<Activity>> getAllActivities() {
        updateActivity();
        return activityList;
    }

    public void addActivity(Activity activity) throws JcalendarTimeConflictException {
        if (isTimeConflict(activity.getDate(), activity.getStartTime())) {
            throw new JcalendarTimeConflictException("Les horaires entrés sont conflictuel !");
        } else {
            activityList.computeIfAbsent(activity.getDate(), k -> new TreeSet<>()).add(activity);
        }
    }

    public void deleteActivity(String d, String st, String et, String activityTitle, String location, String ate) throws JcalendarNoRecordException {
        updateActivity();
        LocalDate date = Jcalendar.stringToLocalDate(d);
        LocalTime startTime = Jcalendar.stringToLocalTime(st);
        LocalTime endTime = Jcalendar.stringToLocalTime(et);
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
                if (recordsInOneDate.size() == 0) {
                    activityList.remove(date);
                }
            }else {
                throw new JcalendarNoRecordException("\n1Record n'existe pas !\n");
            }
        } else {
            throw new JcalendarNoRecordException("\nRecord n'existe pas !\n");
        }
    }

    private void updateActivity() {
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : activityList.entrySet()) {
            LocalDate date = entry.getKey();
            if (JcalendarModel.recordList.containsKey(date)) {
                SortedSet<Activity> recordsInOneDate = entry.getValue();
                for (Activity activity : recordsInOneDate) {
                    if (!JcalendarModel.recordList.get(date).contains(activity)) {
                        activityList.get(date).remove(activity);
                    }
                }
            } else {
                activityList.remove(date);
            }
        }
    }

    public int getRecordsNumber(LocalDate date) throws JcalendarNoRecordException {
        updateActivity();
        if (activityList.containsKey(date)) {
            return activityList.get(date).size();
        } else {
            return 0;
        }
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
                ", prenom='" + prenom + '\'' +
                ", club=" + club +
                ", tarif=" + tarif +
                "\nactivities:\n" + activityList;
    }
}
