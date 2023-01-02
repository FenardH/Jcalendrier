package be.technifutur.jcalendar;

public class Cell {
    private String value = "               ";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return !this.value.equals("               ");
    }

    public void setEmpty() {
        this.value = "               ";
    }

    public void setActityNumber(int num) {
        this.value = String.format("%s évenement(s)", num > 10 ? num : " " + Integer.toString(num));
    }
}
