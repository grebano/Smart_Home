package Interfaces;

import java.util.ArrayList;

/**
 * Interfaccia che rappresenta la callback per la richiesta http
 */
public interface HttpRequestCompleted {
    public void onHttpRequestCompleted (String response);
}
