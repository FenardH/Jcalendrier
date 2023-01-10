package be.technifutur.jcalendar;

import java.io.Serializable;
import java.util.Scanner;

public class ScannerInput implements Input, Serializable {

    private Scanner scan = new Scanner(System.in);
    @Override
    public String read(String message) {
        System.out.print(message);
        return scan.nextLine();
    }
}
