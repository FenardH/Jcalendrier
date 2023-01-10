package be.technifutur.jcalendar;

import java.util.regex.Pattern;

public class Menu {
    public String logo = """
                         ╔╗         ╔╗           ╔╗           \s
                         ║║         ║║           ║║           \s
                         ║║╔══╗╔══╗ ║║ ╔══╗╔═╗ ╔═╝║╔═╗╔╗╔══╗╔═╗
                       ╔╗║║║╔═╝╚ ╗║ ║║ ║╔╗║║╔╗╗║╔╗║║╔╝╠╣║╔╗║║╔╝
                       ║╚╝║║╚═╗║╚╝╚╗║╚╗║║═╣║║║║║╚╝║║║ ║║║║═╣║║\s
                       ╚══╝╚══╝╚═══╝╚═╝╚══╝╚╝╚╝╚══╝╚╝ ╚╝╚══╝╚╝\s                                      
                         """;
    public String welcomePage = String.format("""
                       Veuillez saisir votre nom et prénom (i.e. Olivier,Giroud) ou les commandes suivantes :
                       %s - se connecter en tant que administrateur(trice)
                       %s - s'inscrire comme un(e) nouveau(elle) utilisateur(trice)
                       %s - quitter
                       """, Text.green("admin"), Text.green("N"), Text.green("Q"));

    public String addNewUserTips = """
                       Veuillez saisir votre nom et prénom:
                       prénom,nom OU prénom,nom,club,tarif
                       exemple:
                       Thierry,Henry OU Thierry,Henry,Arsenal,0
                       """;

    public String mainMenuHeader = String.format("""
                         Veuillez saisir votre command avec les instructions suivantes:
                         . - page précédente     . - page suivante
                         . - page Jour          . - page Semaine           . - page Mois
                         . - ajouter une nouvelle activité   . - supprimer une activité
                         . - lister toutes les activités   . - rechercher par date
                         """.replaceAll("\\.", "%s"), Text.green("P"), Text.green("S"),
            Text.green("Jo"), Text.green("Se"), Text.green("Mo"),
            Text.green("A"), Text.green("D"),
            Text.green("L"), Text.green("R"));

    public String mainMenuOptionAdmin = String.format("""
                         . - importer enregistrements   . - exporter enregistrements   . - lister tous les utilisateurs
                         """.replaceAll("\\.", "%s"), Text.green("IMP"), Text.green("EXP"),
            Text.green("LU"));

    public String mainMenuOptionUser = String.format("""
                         . - prendre présence pour activités   . - voir toutes les activités dans le système  
                         """.replaceAll("\\.", "%s"), Text.green("PR"), Text.green("V"));

    public String mainMenuFooter = String.format("""
                         . - changer compte      . - quitter
                         """.replaceAll("\\.", "%s"), Text.green("C"), Text.green("Q"));

    public String mainMenuAdmin = mainMenuHeader + mainMenuOptionAdmin + mainMenuFooter;

    public String mainMenuUser = mainMenuHeader + mainMenuOptionUser + mainMenuFooter;


    public String addNewRecordTips = """
                         Veuillez saisir nouvel enregistrement au format suivant:
                         date(jj/mm/aaaa),heure de début(hh:mm),heure de fin(hh:mm),nom d'activité,localisation,type d'activité(séance/repos/logement)
                         exemple:
                         30/12/2022,09:00,10:00,Python Course,Technifutur,seance
                         """;


    public String searchRecordTips = """
                         Veuillez saisir une date de enregistrement au format suivant:
                         date(jj/mm/aaaa)
                         exemple:
                         30/12/2022
                         """;

    public String importDataTips = """
                        veuillez saisir le chemin de fichier (.csv) :
                        * par défaut : src/be/technifutur/jcalendar/data/testData.csv
                        """;

    public String exportDataTips = """
                        veuillez saisir le chemin pour stocker le fichier (.csv) :
                        * par exemple : src/be/technifutur/jcalendar/data/saveData.csv
                        """;

    public String setPresenceTips = """
                        veuillez saisir indices d'activité et oui ou non représentant la présence au format suivant:
                        indices d'activité indiqués dans la liste au-dessus (nombre),présence(oui/non)
                        exemple: 
                        2,oui,3,non,5,oui
                        """;

    private String rulewelcomemenu = "admin" +  // admin"
                                     "|([a-zA-Z]+," +  // first name
                                     "[a-zA-Z]+" +  // last name
                                     "|([nq])?)"  // command
                                     ;
    public Pattern ruleWelcomeMenu = Pattern.compile(rulewelcomemenu, Pattern.CASE_INSENSITIVE);

    private String rulemainmenu = "p|s|jo|se|mo|a|d|l|r|c|q|imp|exp|lu|pr|v";
    public Pattern ruleMainMenu = Pattern.compile(rulemainmenu, Pattern.CASE_INSENSITIVE);

    private String ruleregistrationmenu = "[a-zA-Z]+," +  // first name
                                          "[a-zA-Z]+" +  // last name
                                          "(,[a-zA-Z]+" +  // club
                                          ",\\d+)?"  // tarif
                                          ;
    public Pattern ruleRegistrationMenu = Pattern.compile(ruleregistrationmenu, Pattern.CASE_INSENSITIVE);

    private String ruledate = "(((0[1-9]|[1-2][0-9]|3[0-1])/(0[13578]|(10|12)))|((0[1-9]|[1-2][0-9])/02)|((0[1-9]|[1-2][0-9]|30)(0[469]|11)))/[0-9]{4}"; // date
    public Pattern ruleDate = Pattern.compile(ruledate, Pattern.CASE_INSENSITIVE);

    private String ruleactivity = ruledate + "," +  // date (dd/MM/yyyy)
                                 "(20|21|22|23|[01]\\d|\\d)((:[0-5]\\d){1,2})," +  // start time (HH:mm)
                                 "(20|21|22|23|[01]\\d|\\d)((:[0-5]\\d){1,2})," +  // end time (HH:mm)
                                 "\\S[\\w ]*," +  // activity name
                                 "\\S[\\w ]*," +  // location
                                 "(seance|repos|logement)," +  // activity type
                                 "\\d+(.\\d|.\\d\\d)?"
                                 ;
    public Pattern ruleActivity = Pattern.compile(ruleactivity, Pattern.CASE_INSENSITIVE);
}
