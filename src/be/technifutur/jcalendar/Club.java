package be.technifutur.jcalendar;

public enum Club {
    ARSENAL, ASTONVILLA, BARNSLEY, BIRMINGHAMCITY, BLACKBURNROVERS, BLACKPOOL,
    BOURNEMOUTH, BRADFORDCITY, BRENTFORD, BRIGHTONHOVEALBION, BURNLEY, CARDIFFCITY,
    CHARLTONATHLETIC, CHELSEA, COVENTRYCITY, CRYSTALPALACE, DERBYCOUNTY, EVERTON,
    FULHAM, HUDDERSFIELDTOWN, HULLCITY, IPSWICHTOWN, LEEDSUNITED, LEICSTERCITY, LIVERPOOL,
    MANCHESTERCITY, MANCHESTERUNITED, MIDDLESBROUGH, NEWCASTLEUNITED, NORWICHCITY, NOTTINGHAMFOREST,
    OLDHAMATHLETIC, PORTSMOUTH, QPR, READING, SHEFFIEDLDUNITED, SOUTHAMPTON, STOKECITY,
    SUNDERLAND, SWANSEACITY, SWINDONTOWN, TECHNIFUTUR, TOTTENHAMHOTSPUR{
        @Override
        public String getMessage() {
            return "Shit";
        }
    }, WALFORD, WESTBROMWICHALBION,
    WESTHAMUNITED, WIGANATHLETIC, WIMBLEDON, WOLVERHAMPTONWANDERERS;

    private String message;

    private Club(String message) {
        this.message = message;
    }

    private Club() {
        this("null");
    }

    public String getMessage() {
        return message;
    }
}
