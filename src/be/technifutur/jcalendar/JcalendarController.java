package be.technifutur.jcalendar;

import be.technifutur.jcalendar.day.ViewDay;
import be.technifutur.jcalendar.month.ViewMonth;
import be.technifutur.jcalendar.week.ViewWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static be.technifutur.jcalendar.Jcalendar.today;

public class JcalendarController {
    private JcalendarModel calendar;
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
    private String logo = """
                         ╔╗         ╔╗           ╔╗           \s
                         ║║         ║║           ║║           \s
                         ║║╔══╗╔══╗ ║║ ╔══╗╔═╗ ╔═╝║╔═╗╔╗╔══╗╔═╗
                       ╔╗║║║╔═╝╚ ╗║ ║║ ║╔╗║║╔╗╗║╔╗║║╔╝╠╣║╔╗║║╔╝
                       ║╚╝║║╚═╗║╚╝╚╗║╚╗║║═╣║║║║║╚╝║║║ ║║║║═╣║║\s
                       ╚══╝╚══╝╚═══╝╚═╝╚══╝╚╝╚╝╚══╝╚╝ ╚╝╚══╝╚╝\s                                      
                         """;
    private String welcomePage = String.format("""
                       Veuillez saisir votre nom et prenom (i.e. Olivier,Giroud) ou les commandes suivantes :
                       %s - se connecter en tant que administrateur(trice)
                       %s - s'inscrire comme un(e) nouveau(elle) utilisateur(trice)
                       %s - quitter
                       """, Text.green("admin"), Text.green("N"), Text.green("Q"));

    private String addNewUserTips = """
                       Veuillez saisir votre nom et prenom:
                       prénom,nom OU prénom,nom,club,tariff
                       example:
                       Thierry,Henry OU Thierry,Henry,Arsenal,0
                       """;

    private String mainMenu = String.format("""
                         Veuillez saisir votre command avec les instructions suivantes:
                         . - page précédente     . - page suivante
                         . - page Jour          . - page Semaine           . - mois
                         . - ajouter une nouvelle activité   . - supprimer une activité
                         . - lister toutes les activité   . - rechcher selon une date 
                         . - changer compte      . - quitter
                         """.replaceAll("\\.", "%s"), Text.green("P"), Text.green("S"),
                                            Text.green("Jo"), Text.green("Se"), Text.green("Mo"),
                                            Text.green("A"), Text.green("D"),
                                            Text.green("L"), Text.green("R"),
                                            Text.green("C"), Text.green("Q"));

    private String addNewRecordTips = """
                         Veuillez saisir nouveau record au format suivant:
                         date(jj/mm/aaaa),heure de début(hh:mm),heure de fin(hh:mm),nom d'activité,localisation,type d'activité(séance/repos/logement)
                         example:
                         30/12/2022,09:00,10:00,Python Course,Technifutur,seance
                         """;


    String searchRecordTips = """
                         Veuillez saisir une date de record au format suivant:
                         date(jj/mm/aaaa)
                         example:
                         30/12/2022
                         """;

    String ruleWelcomeMenu = "admin" +  // admin"
                             "|([a-zA-Z]+," +  // first name
                             "[a-zA-Z]+" +  // last name
                             "|([nq])?)"  // command
                             ;

    String ruleMainMenu = "p|s|jo|se|mo|a|d|l|r|c|q";

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
                          "(seance|repos|logement)"  // activity type
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

    public void start() throws JcalendarException {
        System.out.println(logo);
        welcomePage();
    }

    private void welcomePage() {
        daysToMinusOrPlus = 0;
        weeksToMinusOrPlus = 0;
        monthsToMinusOrPlus = 0;
        String request = "";
        while (!request.equalsIgnoreCase("q")) {
            try {
                request = listenRequestWelcomeMenu();
                if (request.contains(",")) {
                    String[] in = request.split(",");
                    personLogin = getPerson(in[0], in[1]);
                    viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenu), true, false);
                }  else if (request.equalsIgnoreCase("admin")) {
                    viewWeek.displayWeekSchedule(calendar, monthsToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenu), true, true);
                } else if (request.equalsIgnoreCase("n")) {
                    String[] userName = registerPerson(input.read(addNewUserTips));
                    personLogin = getPerson(userName[0], userName[1]);
                    viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(mainMenu), true, false);
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
        while (!request.equalsIgnoreCase("q")){
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
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    }
                } catch (IllegalArgumentException | JcalendarException e) {
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
        while (!request.equalsIgnoreCase("q")){
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
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    }
                } catch (IllegalArgumentException | JcalendarException e) {
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
        while (!request.equalsIgnoreCase("q")){
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
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(addNewRecordTips), isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(searchRecordTips), isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    }
                } catch (IllegalArgumentException | JcalendarException e) {
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
            LocalDate date = Jcalendar.stringToLocalDate(input[0]);
            LocalTime startTime = Jcalendar.stringToLocalTime(input[1]);
            LocalTime endTime = Jcalendar.stringToLocalTime(input[2]);
            String activityTitle = input[3];
            String location = input[4];
            ActivityType activityType = ActivityType.valueOf(input[5].toUpperCase());
            if (isAdmin) {
                calendar.addRecord(new Activity(date, startTime, endTime, activityTitle, location, activityType));
                System.out.println(Text.green("\nActivité est enregistré dans le système !\n"));
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
                            activity.type.equals(activityType)) {
                            personLogin.addActivity(activity);
                            System.out.println(Text.green("\nActivité est enregistré dans votre compte !\n"));
                            isNotFound = false;
                        }
                    }
                    if (isNotFound){
                        throw new JcalendarNoRecordException(Text.red("\ncette activité n'existe pas dans le système !\n"));
                    }
                } else {
                    throw new JcalendarNoRecordException(Text.red("\nauncune activité trouvée dans cette date !\n"));
                }
            }
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entré est invalid !\n"));
        }
    }

    private void deleteRecord(String read, boolean isAdmin) throws JcalendarNoRecordException {
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
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entré est invalid !\n"));
        }
    }

    private void printAllRecords(boolean isAdmin) {
        int count = 0;
        Map<LocalDate, SortedSet<Activity>> allRecords;
        if (isAdmin) {
            allRecords = calendar.getAllRecords();
        } else {
            allRecords = personLogin.getAllActivities();
        }
        for (Map.Entry<LocalDate, SortedSet<Activity>> entry : allRecords.entrySet()) {
            SortedSet<Activity> recordsInOneDate = entry.getValue();
            for (int i = 0; i < recordsInOneDate.size(); i++) {
                System.out.printf("%s\n", recordsInOneDate.toArray()[i]);
                count++;
            }
        }
        System.out.printf(Text.green("%s activitie(s) trouvée(s)\n\n"), count);
    }

    private void findRecord(String read, boolean isAdmin) throws JcalendarNoRecordException {
        if (isInputCorrect(ruleDate, read)) {
            LocalDate date = Jcalendar.stringToLocalDate(read);
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
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entré est invalid !\n"));
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
            System.out.println(Text.green("\ninscription a été effectué avec succès !\n"));
            return Arrays.copyOfRange(input, 0, 2);
        } else {
            throw new IllegalArgumentException(Text.red("\nformat ou valeur d'entré est invalid !\n"));
        }
    }

    public void load() throws JcalendarTimeConflictException {
        JcalendarModel testModel = new JcalendarModel();

        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Python Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("30/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "HTML Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("31/12/2022"), Jcalendar.stringToLocalTime("11:30"), Jcalendar.stringToLocalTime("12:30"), "Python Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:00"), "Java Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("14:00"), Jcalendar.stringToLocalTime("15:00"), "Computer Science Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("02/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("11:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("09:30"), Jcalendar.stringToLocalTime("11:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("15:30"), "Python Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("03/01/2023"), Jcalendar.stringToLocalTime("15:30"), Jcalendar.stringToLocalTime("17:30"), "C++ Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:30"), ".Net Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("10:30"), Jcalendar.stringToLocalTime("12:00"), "TypeScript course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("14:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("04/01/2023"), Jcalendar.stringToLocalTime("14:30"), Jcalendar.stringToLocalTime("15:30"), "Java Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("09:00"), Jcalendar.stringToLocalTime("10:30"), ".Net Course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("11:30"), Jcalendar.stringToLocalTime("12:00"), "TypeScript course", "Technipaste", ActivityType.REPOS));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("13:30"), Jcalendar.stringToLocalTime("14:30"), "JavaScript Course", "Technifutur", ActivityType.SEANCE));
        testModel.addRecord(new Activity(Jcalendar.stringToLocalDate("05/01/2023"), Jcalendar.stringToLocalTime("16:00"), Jcalendar.stringToLocalTime("17:30"), "Java Course", "Technipaste", ActivityType.REPOS));

    }
}
