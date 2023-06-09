package Network;


/**
 * Classe che rappresenta una rete wifi
 */
public class Net {
    private String ssid;
    private String bssid;
    private int level;

    /**
     * Costruttore della rete
     * @param ssid nome della rete
     * @param bssid indirizzo mac della rete
     * @param level livello del segnale
     */
    public Net(String ssid, String bssid, int level)
    {
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
    }

    /**
     * Costruttore di default
     */
    public Net()
    {
        this.ssid = "";
        this.bssid = "";
        this.level = 0;
    }

    /**
     * funzione che ritorna il livello del segnale
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * funzione che ritorna il bssid
     * @return bssid
     */
    public String getBssid() {
        return bssid;
    }

    /**
     * funzione che ritorna l'ssid
     * @return ssid
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * funzione che setta il bssid
     * @param bssid indirizzo mac
     */
    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    /**
     * funzione che setta l'ssid
     * @param ssid nome della rete
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * funzione che setta il livello del segnale
     * @param level livello del segnale
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
