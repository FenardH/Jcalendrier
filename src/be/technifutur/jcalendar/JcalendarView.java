package be.technifutur.jcalendar;

public interface JcalendarView {
    void printBlankCalendar();

    void setMessage(String inputNonValid);

    void printCalendar(JcalendarModel model);
}
