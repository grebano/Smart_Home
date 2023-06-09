package Miscellaneous;

/**
 * Classe che contiene le costanti
 */
public class Constants {
    /**
     * ssid della rete wifi
     */
    public static final String SSID = "enricostefania";
    //--------------------------------------------------------
    /**
     * rotte per le richieste http
     */
    public static final String ON_PATH = "/on";
    public static final String OFF_PATH = "/off";
    public static final String PING_PATH = "/ping";
    public static final String HTTP = "http://";
    public static final String ON_RESPONSE = "on";
    public static final String OFF_RESPONSE = "off";
    //--------------------------------------------------------
    /**
     * costanti per i timer e le azioni ritardate
     */
    public static final int SHUTTER_TIMER_DELAY_IN_MILLIS = 0;
    public static final int SHUTTER_TIMER_PERIOD_IN_MILLIS = 120000;
    public static final int LIGHTS_DELAY_IN_MILLIS = 30000;
    public static int DEVICE_ONLINE_DELAY = 500;
    public static int NIGHT_BEGINNING_TIME = 18;
    public static int MORNING_BEGINNING_TIME = 7;
    //--------------------------------------------------------
    /**
     * costanti per il wifi
     */
    public static int WIFI_NEAR_THRESHOLD = -70;
    public static int WIFI_FAR_THRESHOLD = -80;

}
