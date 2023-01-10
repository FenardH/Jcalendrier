package be.technifutur.jcalendar;

import java.io.Serializable;

public enum ActivityType implements Serializable {
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
