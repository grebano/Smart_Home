package Interfaces;


/**
 * Interfaccia che rappresenta la callback per la richiesta http
 */
public interface HttpRequestCompleted {
    void onHttpRequestCompleted (String response);
}
