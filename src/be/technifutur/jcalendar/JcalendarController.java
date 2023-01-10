package be.technifutur.jcalendar;

import be.technifutur.jcalendar.day.ViewDay;
import be.technifutur.jcalendar.month.ViewMonth;
import be.technifutur.jcalendar.week.ViewWeek;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JcalendarController {
    private JcalendarModel calendar = null;
    private ViewDay viewDay = new ViewDay();
    private ViewWeek viewWeek = new ViewWeek();
    private ViewMonth viewMonth = new ViewMonth();
    private int daysToMinusOrPlus;
    private int weeksToMinusOrPlus;
    private int monthsToMinusOrPlus;
    static List<Person> personList = new ArrayList<>();
    private final Input input = new ScannerInput();
    private String login = "admin";
    private Person personLogin;
    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;
    private String logo = """
                         ╔╗         ╔╗           ╔╗           \s
                         ║║         ║║           ║║           \s
                         ║║╔══╗╔══╗ ║║ ╔══╗╔═╗ ╔═╝║╔═╗╔╗╔══╗╔═╗
                       ╔╗║║║╔═╝╚ ╗║ ║║ ║╔╗║║╔╗╗║╔╗║║╔╝╠╣║╔╗║║╔╝
                       ║╚╝║║╚═╗║╚╝╚╗║╚╗║║═╣║║║║║╚╝║║║ ║║║║═╣║║\s
                       ╚══╝╚══╝╚═══╝╚═╝╚══╝╚╝╚╝╚══╝╚╝ ╚╝╚══╝╚╝\s                                      
                         """;
    private String welcomePage = String.format("""
                       Veuillez saisir votre nom et prénom (i.e. Olivier,Giroud) ou les commandes suivantes :
                       %s - se connecter en tant que administrateur(trice)
                       %s - s'inscrire comme un(e) nouveau(elle) utilisateur(trice)
                       %s - quitter
                       """, Text.green("admin"), Text.green("N"), Text.green("Q"));

    private String addNewUserTips = """
                       Veuillez saisir votre nom et prénom:
                       prénom,nom OU prénom,nom,club,tarif
                       exemple:
                       Thierry,Henry OU Thierry,Henry,Arsenal,0
                       """;

    private String mainMenuHeader = String.format("""
                         Veuillez saisir votre command avec les instructions suivantes:
                         . - page précédente     . - page suivante
                         . - page Jour          . - page Semaine           . - page Mois
                         . - ajouter une nouvelle activité   . - supprimer une activité
                         . - lister toutes les activités   . - rechercher par date
                         """.replaceAll("\\.", "%s"), Text.green("P"), Text.green("S"),
                                            Text.green("Jo"), Text.green("Se"), Text.green("Mo"),
                                            Text.green("A"), Text.green("D"),
                                            Text.green("L"), Text.green("R"));

    private String mainMenuOptionAdmin = String.format("""
                         . - importer enregistrements   . - exporter enregistrements   . - lister tous les utilisateurs
                         """.replaceAll("\\.", "%s"), Text.green("IMP"), Text.green("EXP"),
                                                                      Text.green("LU"));

    private String mainMenuOptionUser = String.format("""
                         . - prendre présence pour activités   . - voir toutes les activités dans le système  
                         """.replaceAll("\\.", "%s"), Text.green("PR"), Text.green("V"));

    private String mainMenuFooter = String.format("""
                         . - changer compte      . - quitter
                         """.replaceAll("\\.", "%s"), Text.green("C"), Text.green("Q"));

    private String mainMenuAdmin = mainMenuHeader + mainMenuOptionAdmin + mainMenuFooter;

    private String mainMenuUser = mainMenuHeader + mainMenuOptionUser + mainMenuFooter;

    private String addNewRecordTips = """
                         Veuillez saisir nouvel enregistrement au format suivant:
                         date(jj/mm/aaaa),heure de début(hh:mm),heure de fin(hh:mm),nom d'activité,localisation,type d'activité(séance/repos/logement)
                         exemple:
                         30/12/2022,09:00,10:00,Python Course,Technifutur,seance
                         """;


    String searchRecordTips = """
                         Veuillez saisir une date de enregistrement au format suivant:
                         date(jj/mm/aaaa)
                         exemple:
                         30/12/2022
                         """;

    String importDataTips = """
                        veuillez saisir le chemin de fichier (.csv) :
                        * par défaut : src/be/technifutur/jcalendar/data/testData.csv
                        """;

    String exportDataTips = """
                        veuillez saisir le chemin pour stocker le fichier (.csv) :
                        * par exemple : src/be/technifutur/jcalendar/data/saveData.csv
                        """;

    String setPresenceTips = """
                        veuillez saisir indices d'activité et oui ou non représentant la présence au format suivant:
                        indices d'activité indiqués dans la liste au-dessus (nombre),présence(oui/non)
                        exemple: 
                        2,oui,3,non,5,oui
                        """;
    String ruleWelcomeMenu = "admin" +  // admin"
                             "|([a-zA-Z]+," +  // first name
                             "[a-zA-Z]+" +  // last name
                             "|([nq])?)"  // command
                             ;

    String ruleMainMenu = "p|s|jo|se|mo|a|d|l|r|c|q|imp|exp|lu|pr|v";

    String ruleRegistrationMenu = "[a-zA-Z]+," +  // first name
                                  "[a-zA-Z]+" +  // last name
                                  "(,[a-zA-Z]+" +  // club
                                  ",\\d+)?"  // tarif
                                  ;

    String ruleDate = "(((0[1-9]|[1-2][0-9]|3[0-1])/(0[13578]|(10|12)))|((0[1-9]|[1-2][0-9])/02)|((0[1-9]|[1-2][0-9]|30)(0[469]|11)))/[0-9]{4}"; // date
    String ruleActivity = ruleDate + "," +  // date (dd/MM/yyyy)
                          "(20|21|22|23|[01]\\d|\\d)((:[0-5]\\d){1,2})," +  // start time (HH:mm)
                          "(20|21|22|23|[01]\\d|\\d)((:[0-5]\\d){1,2})," +  // end time (HH:mm)
                          "\\S[\\w ]*," +  // activity name
                          "\\S[\\w ]*," +  // location
                          "(seance|repos|logement)," +  // activity type
                          "\\d+(.\\d|.\\d\\d)?"
                          ;

    public JcalendarController(JcalendarModel calendar, int daysToMinusOrPlus, int weeksToMinusOrPlus, int monthsToMinusOrPlus) {
        this.calendar = calendar;
        this.daysToMinusOrPlus = daysToMinusOrPlus;
        this.weeksToMinusOrPlus = weeksToMinusOrPlus;
        this.monthsToMinusOrPlus = monthsToMinusOrPlus;
    }

    public JcalendarController(JcalendarModel calendar) {
        this(calendar, 0, 0, 0);
    }

    public void start() {
        deserializer();
        System.out.println(logo);
        welcomePage();
    }

    private void welcomePage() {
        daysToMinusOrPlus = 0;
        weeksToMinusOrPlus = 0;
        monthsToMinusOrPlus = 0;
        String request = "";
        while (true) {
            try {
                request = listenRequestWelcomeMenu();
                if (request.contains(",")) {
                    String[] in = request.split(",");
                    personLogin = getPerson(in[0], in[1]);
                    viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenuUser), true, false);
                }  else if (request.equalsIgnoreCase("admin")) {
                    viewWeek.displayWeekSchedule(calendar, monthsToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenuAdmin), true, true);
                } else if (request.equalsIgnoreCase("n")) {
                    String[] userName = registerPerson(input.read(addNewUserTips));
                    personLogin = getPerson(userName[0], userName[1]);
                    viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenuUser), true, false);
                } else if (request.equalsIgnoreCase("q")) {
                    serializer();
                    System.exit(0);
                }
            } catch (IllegalArgumentException | JcalendarException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String listenRequestWelcomeMenu() {
        String request = input.read(welcomePage);
        if (isInputCorrect(ruleWelcomeMenu, request)) {
            return request;
        } else {
            throw new IllegalArgumentException(Text.red("\ncommande est invalide !\n"));
        }
    }

    private void listenRequestForDayMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarTimeConflictException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? mainMenuAdmin : mainMenuUser;
        while (true){
            if (isInputCorrect(ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        daysToMinusOrPlus -= 1;
                        if (isAdmin) {
                            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
                        } else {
                            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
                        }
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("S")) {
                        daysToMinusOrPlus += 1;
                        if (isAdmin) {
                            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);;
                        } else {
                            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
                        }
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        if (isAdmin) {
                            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
                        } else {
                            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
                        }
                        listenRequestForDayMainMenu(input.read(mainMenu), true,isAdmin);
                    } else if (request.equalsIgnoreCase("Se")) {
                        LocalDate currentDay = calendar.minusOrPlusDays(daysToMinusOrPlus);
                        weeksToMinusOrPlus = (int)ChronoUnit.WEEKS.between(today, currentDay);
                        if (isAdmin) {
                            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
                        } else {
                            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                        }
                        listenRequestForWeekMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        LocalDate currentDay = calendar.minusOrPlusDays(daysToMinusOrPlus);
                        monthsToMinusOrPlus = (int)ChronoUnit.MONTHS.between(today, currentDay);
                        if (isAdmin) {
                            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
                        } else {
                            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
                        }
                        listenRequestForMonthMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    if (isAdmin) {
                        if (request.equalsIgnoreCase("IMP")) {
                            importData(input.read(importDataTips));
                        } else if (request.equalsIgnoreCase("EXP")) {
                            exportData(input.read(exportDataTips));
                        } else if (request.equalsIgnoreCase("LU")) {
                            printPersonRegistrated();
                        }
                    } else {
                        if (request.equalsIgnoreCase("PR")) {
                            printAllRecords(false);
                            setPresencePage(input.read(setPresenceTips));
                            serializer();
                        } else if (request.equalsIgnoreCase("V")) {
                            printAllRecords(true);
                        }
                    }
                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (isAdmin) {
                viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
            } else {
                viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
            }
            listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
        }
    }

    private void listenRequestForWeekMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? mainMenuAdmin : mainMenuUser;
        while (true){
            if (isInputCorrect(ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        weeksToMinusOrPlus -= 1;
                        if (isAdmin) {
                            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
                        } else {
                            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                        }
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("S")) {
                        weeksToMinusOrPlus += 1;
                        if (isAdmin) {
                            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
                        } else {
                            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                        }
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        LocalDate currentDay = calendar.minusOrPlusWeeks(weeksToMinusOrPlus);
                        daysToMinusOrPlus = (int)ChronoUnit.DAYS.between(today, currentDay);
                        if (isAdmin) {
                            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
                        } else {
                            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
                        }
                        listenRequestForDayMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("Se")) {
                        if (isAdmin) {
                            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
                        } else {
                            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                        }
                        listenRequestForWeekMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        LocalDate currentDay = calendar.minusOrPlusWeeks(weeksToMinusOrPlus);
                        monthsToMinusOrPlus = (int)ChronoUnit.MONTHS.between(today, currentDay);
                        if (isAdmin) {
                            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
                        } else {
                            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
                        }
                        listenRequestForMonthMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    if (isAdmin) {
                        if (request.equalsIgnoreCase("IMP")) {
                            importData(input.read(importDataTips));
                        } else if (request.equalsIgnoreCase("EXP")) {
                            exportData(input.read(exportDataTips));
                        } else if (request.equalsIgnoreCase("LU")) {
                            printPersonRegistrated();
                        }
                    } else {
                        if (request.equalsIgnoreCase("PR")) {
                            printAllRecords(false);
                            setPresencePage(input.read(setPresenceTips));
                            serializer();
                        } else if (request.equalsIgnoreCase("V")) {
                            printAllRecords(true);
                        }
                    }
                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (isAdmin) {
                viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
            } else {
                viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
            }
            listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
        }
    }

    private void listenRequestForMonthMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? mainMenuAdmin : mainMenuUser;
        while (true){
            if (isInputCorrect(ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        monthsToMinusOrPlus -= 1;
                        if (isAdmin) {
                            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
                        } else {
                            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
                        }
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("S")) {
                        monthsToMinusOrPlus += 1;
                        if (isAdmin) {
                            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
                        } else {
                            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
                        }
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        LocalDate currentDay = calendar.minusOrPlusMonths(monthsToMinusOrPlus);
                        daysToMinusOrPlus = (int)ChronoUnit.DAYS.between(today, currentDay);
                        if (isAdmin) {
                            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
                        } else {
                            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
                        }
                        listenRequestForDayMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("Se")) {
                        LocalDate currentDay = calendar.minusOrPlusMonths(monthsToMinusOrPlus);
                        weeksToMinusOrPlus = (int)ChronoUnit.WEEKS.between(today, currentDay);
                        if (isAdmin) {
                            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
                        } else {
                            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                        }
                        listenRequestForWeekMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        if (isAdmin) {
                            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
                        } else {
                            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
                        }
                        listenRequestForMonthMainMenu(input.read(mainMenu), true, isAdmin);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    if (isAdmin) {
                        if (request.equalsIgnoreCase("IMP")) {
                            importData(input.read(importDataTips));
                        } else if (request.equalsIgnoreCase("EXP")) {
                            exportData(input.read(exportDataTips));
                        } else if (request.equalsIgnoreCase("LU")) {
                            printPersonRegistrated();
                        }
                    } else {
                        if (request.equalsIgnoreCase("PR")) {
                            printAllRecords(false);
                            setPresencePage(input.read(setPresenceTips));
                            serializer();
                        } else if (request.equalsIgnoreCase("V")) {
                            printAllRecords(true);
                        }
                    }
                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (isAdmin) {
                viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
            } else {
                viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
            }
            listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
        }
    }

    private boolean isInputCorrect(String rule, String read) {
        Pattern pattern = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(read);
        if (matcher.matches()) {
            return true;
        } else {
            throw new IllegalArgumentException(Text.red("\ncommande est invalide !\n"));
        }
    }

    private void addRecord(String read, boolean isAdmin) throws JcalendarTimeConflictException, JcalendarNoRecordException {
        if (isInputCorrect(ruleActivity, read)) {
            String[] input = read.split(",");
            LocalDate date = jcalendar.stringToLocalDate(input[0]);
            LocalTime startTime = jcalendar.stringToLocalTime(input[1]);
            LocalTime endTime = jcalendar.stringToLocalTime(input[2]);
            String activityTitle = input[3];
            String location = input[4];
            ActivityType activityType = ActivityType.valueOf(input[5].toUpperCase());
            double tarif = Double.valueOf(input[6]);
            if (isAdmin) {
                calendar.addRecord(new Activity(date, startTime, endTime, activityTitle, location, activityType, tarif));
                System.out.println(Text.green("\nActivité est enregistrée dans le système !\n"));
            } else {
                Optional<SortedSet<Activity>> allActivityInTheDay = calendar.getRecord(date);
                if (allActivityInTheDay.isPresent()) {
                    SortedSet<Activity> activities = allActivityInTheDay.get();
                    boolean isNotFound = true;
                    for (Activity activity : activities) {
                        if (activity.startTime.equals(startTime) &&
                            activity.endTime.equals(endTime) &&
                            activity.activityTitle.equals(activityTitle) &&
                            activity.location.equals(location) &&
                            activity.type.equals(activityType) &&
                            activity.tarif.equals(tarif)) {
                            personLogin.addActivity(activity);
                            System.out.println(Text.green("\nActivité est enregistrée dans votre compte !\n"));
                            isNotFound = false;
                        }
                    }
                    if (isNotFound){
                        throw new JcalendarNoRecordException(Text.red("\ncette activité n'existe pas dans le système !\n"));
                    }
                } else {
                    throw new JcalendarNoRecordException(Text.red("\nauncune activité trouvée à cette date !\n"));
                }
            }
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entrée est invalide !\n"));
        }
    }

    private void deleteRecord(String read, boolean isAdmin) throws JcalendarNoRecordException, JcalendarTimeConflictException {
        if (isInputCorrect(ruleActivity, read)) {
            String[] input = read.split(",");
            if (isAdmin) {
                calendar.deleteRecord(input[0], input[1], input[2], input[3], input[4], input[5]);
                System.out.println(Text.green("\nActivité est supprimée !\n"));
            } else {
                personLogin.deleteActivity(input[0], input[1], input[2], input[3], input[4], input[5]);
                System.out.println(Text.green("\nActivité est supprimée de votre compte !\n"));
            }
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entrée est invalide !\n"));
        }
    }

    private void setPresencePage(String read) {
        String[] input = read.split(",");
        Activity[] personActivities = personLogin.getPresenceList().keySet().toArray(new Activity[0]);
        Activity activity;
        try {
            for (int i = 0; i < input.length / 2; i++) {
                activity = personActivities[Integer.parseInt(input[i*2])-1];
                boolean isPrence;
                if (input[i*2+1].equalsIgnoreCase("oui")) {
                    isPrence = true;
                } else if (input[i*2+1].equalsIgnoreCase("non")) {
                    isPrence = false;
                } else {
                    throw new IllegalArgumentException(Text.red("\nformat d'entrée est invalide !\n"));
                }
                personLogin.setPresence(activity, isPrence);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void printAllRecords(boolean isAdmin) throws JcalendarTimeConflictException {
        int count = 0;
        if (isAdmin) {
            calendar.getAllRecordsAsList(true);
            count = calendar.getRecordsNumber();
        } else {
            personLogin.getAllRecordsAsList(true);
            count = personLogin.getRecordsNumber();
            System.out.println("\nFrais totaux: " + personLogin.getTotalFee() + "\n");
        }
        System.out.printf(Text.green("%s activité(s) trouvée(s)\n\n"), count);
    }

    private void findRecord(String read, boolean isAdmin) throws JcalendarNoRecordException, JcalendarTimeConflictException {
        if (isInputCorrect(ruleDate, read)) {
            LocalDate date = jcalendar.stringToLocalDate(read);
            if (isAdmin) {
                Optional<SortedSet<Activity>> activities = calendar.getRecord(date);
                if (activities.isPresent()) {
                    SortedSet<Activity> actsDay = activities.get();
                    for (int i = 0; i < actsDay.size(); i ++) {
                        System.out.printf("[%s] %s\n", i + 1, actsDay.toArray()[i]);
                    }
                } else {
                    throw new JcalendarNoRecordException(Text.red("\nAucun historique trouvé !\n"));
                }
            } else {
                Optional<SortedSet<Activity>> activities = personLogin.getActivity(date);
                if (activities.isPresent()) {
                    SortedSet<Activity> actsDay = activities.get();
                    for (int i = 0; i < actsDay.size(); i ++) {
                        System.out.printf("[%s] %s\n", i + 1, actsDay.toArray()[i]);
                    }
                } else {
                    throw new JcalendarNoRecordException(Text.red("\nAucun historique trouvé !\n"));
                }
            }
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entrée est invalide !\n"));
        }
    }

    private void addPerson(String firstName, String lastName, Club club, int tarif) {
        personList.add(new Person(firstName, lastName, club, tarif));
    }

    private void addPerson(String firstName, String lastName) {
        personList.add(new Person(firstName, lastName));
    }

    private Person getPerson(String firstName, String lastName) {
        for (Person person : personList) {
            if(person.getPrenom().equalsIgnoreCase(firstName) && person.getNom().equalsIgnoreCase(lastName)) {
                return person;
            }
        }
        throw new IllegalArgumentException(Text.red("\nutilisateur(trice) n'existe pas !\n"));
    }

    private String[] registerPerson(String read) {
        if (isInputCorrect(ruleRegistrationMenu, read)) {
            String[] input = read.split(",");
            if (input.length == 2) {
                addPerson(input[0], input[1]);
            } else if (input.length == 4) {
                addPerson(input[0], input[1], Club.valueOf(input[2].toUpperCase()), Integer.valueOf(input[3]));
            }
            System.out.println(Text.green("\ninscription a été effectuée avec succès !\n"));
            return Arrays.copyOfRange(input, 0, 2);
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entrée est invalide !\n"));
        }
    }

    private void printPersonRegistrated() {
        for (Person person : personList) {
            System.out.printf("%s,%s\n", person.getPrenom(), person.getNom());
        }
        System.out.printf(Text.green("%s utilisateur(s) trouvé(s) !\n"), personList.size());
    }

    public void importData(String path) throws JcalendarTimeConflictException, IOException {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)
            {
                String[] record = line.split(",");
                calendar.addRecord(new Activity(jcalendar.stringToLocalDate(record[0]),
                        jcalendar.stringToLocalTime(record[1]),
                        jcalendar.stringToLocalTime(record[2]),
                        record[3],
                        record[4],
                        ActivityType.valueOf(record[5].toUpperCase()),
                        Double.valueOf(record[6].equals("") ? String.valueOf(0) : record[6])));
            }
            System.out.println(Text.green("\nchargement de données a été effectué avec succès !\n"));
        } catch (IOException e) {
            throw new IllegalArgumentException(Text.red("chemin d'entrée est invalide ou n'existe pas !"));
        }
    }

    public void exportData(String path) throws IOException {
        List<Activity> allRecords = calendar.getAllRecordsAsList(false);
        FileWriter csvWriter = new FileWriter(path);
        for (Activity activity : allRecords) {
            String record = jcalendar.localDateToString(activity.getDate()) + "," +
                            jcalendar.localTimeToString(activity.getStartTime()) + "," +
                            jcalendar.localTimeToString(activity.getEndTime()) + "," +
                            activity.getActivityTitle() + "," +
                            activity.getLocation() + "," +
                            activity.getType().toString().toLowerCase() + "," +
                            activity.getTarif() + "\n"
                            ;
            csvWriter.append(record);
        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println(Text.green("\nexportation de données a été effectuée avec succès !\n"));
    }

    private void serializer() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream file = new FileOutputStream("checkpoint.ser");
            SerialisationContainer checkpoint = new SerialisationContainer(calendar.getAllRecords(), personList);
            oos = new ObjectOutputStream(file);
            oos.writeObject(checkpoint);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deserializer() {
        ObjectInputStream ois = null;
        try {
            FileInputStream file = new FileInputStream("checkpoint.ser");
            ois = new ObjectInputStream(file);
            SerialisationContainer checkpoint  = (SerialisationContainer) ois.readObject();
            calendar.setRecordList(checkpoint.getRecordList());
            personList = checkpoint.getPersonList();
        } catch (IOException e) {
            ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
