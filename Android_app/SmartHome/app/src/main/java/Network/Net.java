package Network;

public class Net {
    private String ssid;
    private String bssid;
    private int level;

    public Net(String ssid, String bssid, int level)
    {
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
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
}
