package be.technifutur.jcalendar;

import java.io.*;

public class Main {
    private static ScannerInput input = new ScannerInput();

    public static void main(String[] args) throws JcalendarException, IOException {
        calendar();
    }

    private static void calendar() throws JcalendarException {
        JcalendarModel model = new JcalendarModel();
        JcalendarController controleur = new JcalendarController(model);

        controleur.start();
    }
}