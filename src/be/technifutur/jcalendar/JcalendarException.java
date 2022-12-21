package be.technifutur.jcalendar;

public class JcalendarException extends Exception{
    public JcalendarException() {
    }

    public JcalendarException(String message) {
        super(message);
    }

    public JcalendarException(String message, Throwable cause) {
        super(message, cause);
    }

    public JcalendarException(Throwable cause) {
        super(cause);
    }

    public JcalendarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
