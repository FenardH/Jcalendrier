package be.technifutur.jcalendar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface JcalendarModel {
    String EMPTY = "";

    List<Activity> list = new ArrayList<>();

    Optional<List<Activity>> getRecord(Person person);

    Optional<List<Activity>>  getRecord(Time time);

    Optional<List<Activity>>  getRecord(String location);

    void setRecord(Activity activity);

    void deleteRecord(int lig, int col);

    boolean isTimeValid(char value);

    boolean isPersonAvailable(Person p);

    int getNbRecords();

    int getMaxSize();

    void lock();

    void loadPreset(char[] gameToLoad);
}
