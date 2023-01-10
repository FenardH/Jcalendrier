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
    Menu menu = new Menu();
    static List<Person> personList = new ArrayList<>();
    private final Input input = new ScannerInput();
    private String login = "admin";
    private Person personLogin;
    Jcalendar jcalendar = new Jcalendar();
    LocalDate today = jcalendar.today;

    public JcalendarController(JcalendarModel calendar) {
        this.calendar = calendar;
    }

    public void start() {
        deserializer();
        System.out.println(menu.logo);
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
                    listenRequestForWeekMainMenu(input.read(menu.mainMenuUser), true, false);
                    request = "q";
                }  else if (request.equalsIgnoreCase("admin")) {
                    viewWeek.displayWeekSchedule(calendar, monthsToMinusOrPlus);
                    request = "q";
                    listenRequestForWeekMainMenu(input.read(menu.mainMenuAdmin), true, true);
                    request = "q";
                } else if (request.equalsIgnoreCase("n")) {
                    String[] userName = registerPerson(input.read(menu.addNewUserTips));
                    personLogin = getPerson(userName[0], userName[1]);
                    viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
                    listenRequestForWeekMainMenu(input.read(menu.mainMenuUser), true, false);
                    request = "q";
                }
            } catch (IllegalArgumentException | JcalendarException e) {
                System.out.println(e.getMessage());
            }
        }
        serializer();
        System.exit(0);
    }

    private String listenRequestWelcomeMenu() {
        String request = input.read(menu.welcomePage);
        if (isInputCorrect(menu.ruleWelcomeMenu, request)) {
            return request;
        } else {
            throw new IllegalArgumentException(Text.red("\ncommande est invalide !\n"));
        }
    }

    private void listenRequestForDayMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarTimeConflictException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? menu.mainMenuAdmin : menu.mainMenuUser;
        while (true){
            if (isInputCorrect(menu.ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        daysToMinusOrPlus -= 1;
                        request = executeRequestForDayMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("S")) {
                        daysToMinusOrPlus += 1;
                        request = executeRequestForDayMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        request = executeRequestForDayMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Se")) {
                        LocalDate currentDay = calendar.minusOrPlusDays(daysToMinusOrPlus);
                        weeksToMinusOrPlus = (int)ChronoUnit.WEEKS.between(today, currentDay);
                        request = executeRequestForWeekMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        LocalDate currentDay = calendar.minusOrPlusDays(daysToMinusOrPlus);
                        monthsToMinusOrPlus = (int)ChronoUnit.MONTHS.between(today, currentDay);
                        request = executeRequestForMonthMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(menu.searchRecordTips), isAdmin);
                        listenRequestForDayMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                        request = "q";
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    request = executeOptionalMenu(request, isAdmin);
                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            request = executeRequestForDayMainMenu(isAdmin, mainMenu, false);
        }
    }

    private void listenRequestForWeekMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? menu.mainMenuAdmin : menu.mainMenuUser;
        while (true){
            if (isInputCorrect(menu.ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        weeksToMinusOrPlus -= 1;
                        request = executeRequestForWeekMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("S")) {
                        weeksToMinusOrPlus += 1;
                        request = executeRequestForWeekMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        LocalDate currentDay = calendar.minusOrPlusWeeks(weeksToMinusOrPlus);
                        daysToMinusOrPlus = (int)ChronoUnit.DAYS.between(today, currentDay);
                        request = executeRequestForDayMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Se")) {
                        request = executeRequestForWeekMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        LocalDate currentDay = calendar.minusOrPlusWeeks(weeksToMinusOrPlus);
                        monthsToMinusOrPlus = (int)ChronoUnit.MONTHS.between(today, currentDay);
                        request = executeRequestForMonthMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(menu.searchRecordTips), isAdmin);
                        listenRequestForWeekMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                        request = "q";
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    request = executeOptionalMenu(request, isAdmin);
                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            request = executeRequestForWeekMainMenu(isAdmin, mainMenu, false);
        }
    }

    private void listenRequestForMonthMainMenu(String request, boolean reset, boolean isAdmin) throws JcalendarException {
        if (reset) {daysToMinusOrPlus = weeksToMinusOrPlus = monthsToMinusOrPlus = 0;}
        String mainMenu = isAdmin ? menu.mainMenuAdmin : menu.mainMenuUser;
        while (true){
            if (isInputCorrect(menu.ruleMainMenu, request)) {
                try {
                    if (request.equalsIgnoreCase("P")) {
                        monthsToMinusOrPlus -= 1;
                        request = executeRequestForMonthMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("S")) {
                        monthsToMinusOrPlus += 1;
                        request = executeRequestForMonthMainMenu(isAdmin, mainMenu, false);
                    } else if (request.equalsIgnoreCase("Jo")) {
                        LocalDate currentDay = calendar.minusOrPlusMonths(monthsToMinusOrPlus);
                        daysToMinusOrPlus = (int)ChronoUnit.DAYS.between(today, currentDay);
                        request = executeRequestForDayMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Se")) {
                        LocalDate currentDay = calendar.minusOrPlusMonths(monthsToMinusOrPlus);
                        weeksToMinusOrPlus = (int)ChronoUnit.WEEKS.between(today, currentDay);
                        request = executeRequestForWeekMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("Mo")) {
                        request = executeRequestForMonthMainMenu(isAdmin, mainMenu, true);
                    } else if (request.equalsIgnoreCase("A")) {
                        addRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("D")) {
                        deleteRecord(input.read(menu.addNewRecordTips), isAdmin);
                        serializer();
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("L")) {
                        printAllRecords(isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("R")) {
                        findRecord(input.read(menu.searchRecordTips), isAdmin);
                        listenRequestForMonthMainMenu(input.read(mainMenu), false, isAdmin);
                        request = "q";
                    } else if (request.equalsIgnoreCase("C")) {
                        welcomePage();
                    } else if (request.equalsIgnoreCase("q")) {
                        serializer();
                        System.exit(0);
                    }
                    request = executeOptionalMenu(request, isAdmin);

                } catch (IllegalArgumentException | JcalendarException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            request = executeRequestForMonthMainMenu(isAdmin, mainMenu, false);
        }
    }

    private String executeRequestForDayMainMenu(boolean isAdmin, String mainMenu, boolean reset) throws JcalendarTimeConflictException {
        if (isAdmin) {
            viewDay.displayDaySchedule(calendar, daysToMinusOrPlus);
        } else {
            viewDay.displayDaySchedule(personLogin, daysToMinusOrPlus);
        }
        listenRequestForDayMainMenu(input.read(mainMenu), reset, isAdmin);
        return "q";
    }

    private String executeRequestForWeekMainMenu(boolean isAdmin, String mainMenu, boolean reset) throws JcalendarException {
        if (isAdmin) {
            viewWeek.displayWeekSchedule(calendar, weeksToMinusOrPlus);
        } else {
            viewWeek.displayWeekSchedule(personLogin, weeksToMinusOrPlus);
        }
        listenRequestForWeekMainMenu(input.read(mainMenu), reset, isAdmin);
        return "q";
    }

    private String executeRequestForMonthMainMenu(boolean isAdmin, String mainMenu, boolean reset) throws JcalendarException {
        if (isAdmin) {
            viewMonth.displayMonthSchedule(calendar, monthsToMinusOrPlus);
        } else {
            viewMonth.displayMonthSchedule(personLogin, monthsToMinusOrPlus);
        }
        listenRequestForMonthMainMenu(input.read(mainMenu), reset, isAdmin);
        return "q";
    }

    private String executeOptionalMenu(String request, boolean isAdmin) throws JcalendarTimeConflictException, IOException {
//        if (isAdmin) {
//            if (request.equalsIgnoreCase("IMP")) {
//                importData(input.read(menu.importDataTips));
//            } else if (request.equalsIgnoreCase("EXP")) {
//                exportData(input.read(menu.exportDataTips));
//            } else if (request.equalsIgnoreCase("LU")) {
//                printPersonRegistrated();
//            }
//        } else {
//            if (request.equalsIgnoreCase("PR")) {
//                printAllRecords(false);
//                setPresencePage(input.read(menu.setPresenceTips));
//                serializer();
//            } else if (request.equalsIgnoreCase("V")) {
//                printAllRecords(true);
//            }
//        }

        Map<String,Runnable> command = new LinkedHashMap<>();

        if (isAdmin) {
            command.put("IMP", () -> {
                try {
                    importData(input.read(menu.importDataTips));
                }
                catch (JcalendarTimeConflictException e) {
                    throw new RuntimeException(e);
                }
            });
            command.put("EXP", () -> {
                try {
                    exportData(input.read(menu.exportDataTips));
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            command.put("LU", () -> {
                try {
                    printAllRecords(true);
                }
                catch (JcalendarTimeConflictException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            command.put("PR", () -> {
                try {
                    printAllRecords(false);
                    setPresencePage(input.read(menu.setPresenceTips));
                    serializer();
                }
                catch (JcalendarTimeConflictException e) {
                    throw new RuntimeException(e);
                }
            });
            command.put("V", () -> {
                try {
                    printAllRecords(false);
                }
                catch (JcalendarTimeConflictException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        try {
            command.get(request.toUpperCase()).run();
        }
        catch (Exception e) {
            ;
        }
        return "q";
    }

    private boolean isInputCorrect(Pattern pattern, String read) {
        Matcher matcher = pattern.matcher(read);
        if (matcher.matches()) {
            return true;
        } else {
            throw new IllegalArgumentException(Text.red("\ncommande est invalide !\n"));
        }
    }

    private void addRecord(String read, boolean isAdmin) throws JcalendarTimeConflictException, JcalendarNoRecordException {
        if (isInputCorrect(menu.ruleActivity, read)) {
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
        if (isInputCorrect(menu.ruleActivity, read)) {
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
        if (isInputCorrect(menu.ruleDate, read)) {
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
        if (isInputCorrect(menu.ruleRegistrationMenu, read)) {
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
        System.out.printf(Text.green("%s utilisateur(s) trouvé(s) !\n\n"), personList.size());
    }

    public void importData(String path) throws JcalendarTimeConflictException {
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
