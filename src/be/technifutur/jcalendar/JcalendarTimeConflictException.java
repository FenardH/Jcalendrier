package be.technifutur.jcalendar;

public class JcalendarTimeConflictException extends JcalendarException {
    public JcalendarTimeConflictException() {
    }

    public JcalendarTimeConflictException(String message) {
        super(message);
    }

    public JcalendarTimeConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public JcalendarTimeConflictException(Throwable cause) {
        super(cause);
    }

    public JcalendarTimeConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
