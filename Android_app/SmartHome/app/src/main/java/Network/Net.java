package Network;

public class Net {
    private String ssid;
    private String bssid;
    private int level;

    // costruttore con parametri
    public Net(String ssid, String bssid, int level)
    {
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
    }

    // costruttore di default
    public Net()
    {
        this.ssid = "";
        this.bssid = "";
        this.level = 0;
    }

    public int getLevel() {
        return level;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
