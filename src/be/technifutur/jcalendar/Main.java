package be.technifutur.jcalendar;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    private static ScannerInput input = new ScannerInput();

    public static void main(String[] args) throws JcalendarException, IOException {
        calendar();
    }

    private static void calendar() throws JcalendarException, IOException {
        JcalendarModel model = new JcalendarModel();
        JcalendarController controleur = new JcalendarController(model);

        controleur.start();
    }
}