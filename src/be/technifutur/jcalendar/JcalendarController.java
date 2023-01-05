package be.technifutur.jcalendar;

import be.technifutur.jcalendar.day.ViewDay;
import be.technifutur.jcalendar.month.ViewMonth;
import be.technifutur.jcalendar.week.ViewWeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class JcalendarController {
    private JcalendarModel calendar;
    private ViewDay viewDay = new ViewDay();
    private ViewWeek viewWeek = new ViewWeek();
    private ViewMonth viewMonth = new ViewMonth();
    private int daysToMinusOrAdd;
    private int weeksToMinusOrAdd;
    private int monthsToMinusOrAdd;
    static List<Person> personList = new ArrayList<>();
    private final Input input = new ScannerInput();

    public JcalendarController(JcalendarModel calendar, int daysToMinusOrAdd, int weeksToMinusOrAdd, int monthsToMinusOrAdd) {
        this.calendar = calendar;
        this.daysToMinusOrAdd = daysToMinusOrAdd;
        this.weeksToMinusOrAdd = weeksToMinusOrAdd;
        this.monthsToMinusOrAdd = monthsToMinusOrAdd;
    }

    public JcalendarController(JcalendarModel calendar) {
        this(calendar, 0, 0, 0);
    }

    public void start() {
        String logo = """
                         ╔╗         ╔╗           ╔╗           \s
                         ║║         ║║           ║║           \s
                         ║║╔══╗╔══╗ ║║ ╔══╗╔═╗ ╔═╝║╔═╗╔╗╔══╗╔═╗
                       ╔╗║║║╔═╝╚ ╗║ ║║ ║╔╗║║╔╗╗║╔╗║║╔╝╠╣║╔╗║║╔╝
                       ║╚╝║║╚═╗║╚╝╚╗║╚╗║║═╣║║║║║╚╝║║║ ║║║║═╣║║\s
                       ╚══╝╚══╝╚═══╝╚═╝╚══╝╚╝╚╝╚══╝╚╝ ╚╝╚══╝╚╝\s                                      
                         """;
        String welcomePage = String.format("""
                       Veuillez saisir votre nom et prenom (i.e. Olivier Giroud)
                       * Si vous êtes administrateur(trice), veuillez entrer %s
                       * Si vous êtes un(e) nouveau(elle) utilisateur(trice), veuillez entrer %s pour vous inscrire
                       %s - quitter
                       """, TextColor.red("admin"), TextColor.green("N"), TextColor.green("Q"));

        String inscriptionPage = """
                       Veuillez saisir votre nom et prenom (i.e. Thierry Henry ou Thierry Henry Arsenal 0)
                       """;

        String mainMenu = String.format("""
                         Veuillez saisir votre command avec les instructions suivantes:
                         . - page précédente     . - page suivante
                         . - page Jour          . - page Semaine           . - mois
                         . - ajouter un nouveau record   . - rechcher selon une date 
                         . - changer compte      . - quitter
                         """.replaceAll("\\.", "%s"), TextColor.green("P"), TextColor.green("S"),
                                                TextColor.green("Jo"), TextColor.green("Se"), TextColor.green("Mo"),
                                                TextColor.green("A"), TextColor.green("R"),
                                                TextColor.green("C"), TextColor.green("Q"));

        System.out.println(logo);
        String request = input.read(welcomePage);

        while(!request.equalsIgnoreCase("q")){
            try {
                String[] name;
                if (request.equalsIgnoreCase("admin")) {

                } else if (request.equalsIgnoreCase("N")) {
                    request = input.read(inscriptionPage);
                    registrePerson(request);
                    System.out.println(personList);
                } else {
                    name = request.split(" ");
                    if (isPersonExist(name[0], name[1])) {

                    } else {
                        request = input.read(inscriptionPage);
                        registrePerson(request);
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Veuillez saisir au format correct !");
            }
        }
    }

    private void addPerson(String firstName, String lastName, Club club, int tarif) {
        personList.add(new Person(firstName, lastName, club, tarif));
    }

    private void addPerson(String firstName, String lastName) {
        personList.add(new Person(firstName, lastName));
    }

    private void registrePerson(String request) {
        String[] name = request.split(" ");
        if (name.length == 2) {
            addPerson(name[0], name[1]);
        } else if (name.length == 4) {
            addPerson(name[0], name[1], Club.valueOf(name[2]), Integer.valueOf(name[3]));
        } else {
            throw new IllegalArgumentException("format ou valeur d'entré est invalid !");
        }
    }
    private boolean isPersonExist(String firsName, String lastName) {
        for (Person p : personList) {
            if(p.getPrenom() == firsName && p.getNom() == lastName) {
                return true;
            }
        }
        return false;
    }
//        vue.afficherGrille();
//        String request = input.read("Modififier (lig.col.valeur), Supprimer (lig.col), Restart (r) , Quitter (q) :");
//        while(!request.equalsIgnoreCase("q")){
//            if (request.equalsIgnoreCase("r")) {
//                sudokuGame();
//            }
//            Matcher matcher = pattern.matcher(request);
//            if(matcher.matches()){
//                int lig = Integer.parseInt(matcher.group(1))-1;
//                int col = Integer.parseInt(matcher.group(2))-1;
//                String value = matcher.group(3);
//                try {
//                    if (value != null) {
//                        char val = value.charAt(1);
//                        sudoku.setValue(lig, col, val);
//                    } else {
//                        sudoku.deleteValue(lig, col);
//                    }
//                }catch(SudokuException e){
//                    vue.setMessage(e.getMessage());
//                }
//
//            }else{
//                vue.setMessage("entrée non valide");
//            }
//            vue.afficherGrille();
//            request = input.read("Modififier (lig.col.valeur), Supprimer (lig.col), Restart (r) , Quitter (q) :");
//        }
//    }
}
