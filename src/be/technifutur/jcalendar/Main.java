package be.technifutur.jcalendar;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    private static ScannerInput input = new ScannerInput();

    public static void main(String[] args) throws JcalendarException {
        calendar();
    }

    private static void calendar() throws JcalendarException {
        JcalendarModel model = new JcalendarModel();
        JcalendarController controleur = new JcalendarController(model);

        controleur.load();

        controleur.start();
    }
}