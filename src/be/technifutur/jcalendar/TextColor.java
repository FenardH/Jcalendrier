package be.technifutur.jcalendar;

public class TextColor {
    private static final String ANSI_RESET = "\u001B[0m";

    public static String black(String text) {
        String ANSI_BLACK = "\u001B[30m";
        return new String(ANSI_BLACK + text + ANSI_RESET);
    }

    public static String red(String text) {
        String ANSI_RED = "\u001B[31m";
        return new String(ANSI_RED + text + ANSI_RESET);
    }

    public static String green(String text) {
        String ANSI_GREEN = "\u001B[32m";
        return new String(ANSI_GREEN + text + ANSI_RESET);
    }

    public static String yellow(String text) {
        String ANSI_YELLOW = "\u001B[33m";
        return new String(ANSI_YELLOW + text + ANSI_RESET);
    }

    public static String blue(String text) {
        String ANSI_BLUE = "\u001B[34m";
        return new String(ANSI_BLUE + text + ANSI_RESET);
    }

    public static String purple(String text) {
        String ANSI_PURPLE = "\u001B[35m";
        return new String(ANSI_PURPLE + text + ANSI_RESET);
    }

    public static String cyan(String text) {
        String ANSI_CYAN = "\u001B[36m";
        return new String(ANSI_CYAN + text + ANSI_RESET);
    }

    public static String white(String text) {
        String ANSI_WHITE = "\u001B[37m";
        return new String(ANSI_WHITE + text + ANSI_RESET);
    }

//    public static void main(String[] args) {
//        System.out.println(black("Hello World"));
//        System.out.println(red("Hello World"));
//        System.out.println(green("Hello World"));
//        System.out.println(yellow("Hello World"));
//        System.out.println(blue("Hello World"));
//        System.out.println(purple("Hello World"));
//        System.out.println(cyan("Hello World"));
//        System.out.println(white("Hello World"));
//    }
}
