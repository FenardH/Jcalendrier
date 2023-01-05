package be.technifutur.jcalendar;

public class JcalendarNoRecordException  extends JcalendarException {
    public JcalendarNoRecordException() {
    }

    public JcalendarNoRecordException(String message) {
        super(message);
    }

    public JcalendarNoRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public JcalendarNoRecordException(Throwable cause) {
        super(cause);
    }

    public JcalendarNoRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
