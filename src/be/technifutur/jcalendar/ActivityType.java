package be.technifutur.jcalendar;

public enum ActivityType {
    SEANCE, REPOS, LOGEMENT;

    private String message;

    private ActivityType(String message) {
        this.message = message;
    }

    private ActivityType() {
        this("null");
    }

    public String getMessage() {
        return message;
    }
}
