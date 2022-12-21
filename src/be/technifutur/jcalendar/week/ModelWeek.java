package be.technifutur.jcalendar.week;

import be.technifutur.jcalendar.Activity;
import be.technifutur.jcalendar.JcalendarModel;
import be.technifutur.jcalendar.Person;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelWeek implements JcalendarModel {
    private String[] header;
    private Map<Time, Activity> records;


    @Override
    public Optional<List<Activity>> getRecord(Person person) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Activity>> getRecord(Time time) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Activity>> getRecord(String location) {
        return Optional.empty();
    }

    @Override
    public void setRecord(Activity activity) {

    }

    @Override
    public void deleteRecord(int lig, int col) {

    }

    @Override
    public boolean isTimeValid(char value) {
        return false;
    }

    @Override
    public boolean isPersonAvailable(Person p) {
        return false;
    }

    @Override
    public int getNbRecords() {
        return 0;
    }

    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public void lock() {

    }

    @Override
    public void loadPreset(char[] gameToLoad) {

    }
}
