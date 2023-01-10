package be.technifutur.jcalendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class SerialisationContainer implements Serializable {
    Map<LocalDate, SortedSet<Activity>> recordList;
    private List<Person> personList;

    public SerialisationContainer(Map<LocalDate, SortedSet<Activity>> recordList, List<Person> personList) {
        this.recordList = recordList;
        this.personList = personList;
    }

    public Map<LocalDate, SortedSet<Activity>> getRecordList() {
        return recordList;
    }

    public List<Person> getPersonList() {
        return personList;
    }
}
