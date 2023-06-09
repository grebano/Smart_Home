package Network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Interfaces.HttpRequestCompleted;

/**
 * Classe che gestisce le richieste http
 */

public class HttpRequests {
    private final String TAG = "HttpRequest";
    private final String url;

    // oggetti dell'interfaccia usata per restituire informazioni (http response)
    ArrayList<HttpRequestCompleted> httpRequestList;


    /**
     * Costruttore della classe, con associazione alla classe http request (in ingresso ogg. interfaccia)
     * @param url indirizzo ip del dispositivo
     * @param givenHttpRequestList interfaccia per la gestione della risposta http
     */
    public HttpRequests(String url, ArrayList<HttpRequestCompleted> givenHttpRequestList)
    {
        this.url = url;
        httpRequestList = new ArrayList<>(givenHttpRequestList);
    }


    /**
     * wrapper per la richiesta http
     * @param path path della richiesta
     */
    public void Request(String path)
    {
        DoaRequest(this.url,path);
    }


    /**
     * funzione che restituisce l'url del dispositivo
     */
    public String getUrl()
    {
        return this.url;
    }


    /**
     * Metodo che effettua la richiesta http
     * @param url url base
     * @param path path della richiesta
     */
    private void DoaRequest(String url, String path)
    {
        new Thread(() -> {
            StringBuilder strContents = new StringBuilder();
            URL myUrl = null;
            // creazione dell'url completo
            try {
                myUrl = new URL(url + path);
            } catch (MalformedURLException e) {
                Log.e(TAG,"Error connecting to device via http - bad url");
            }
            HttpURLConnection urlConnection = null;
            try {
                if (myUrl != null) {
                    urlConnection = (HttpURLConnection) myUrl.openConnection();
                }
                try {
                    try {

                        // apertura e lettura dallo stream http
                        BufferedInputStream in = null;
                        if (urlConnection != null) {
                            in = new BufferedInputStream(urlConnection.getInputStream());
                        }

                        byte[] contents = new byte[1024];
                        int bytesRead;
                        if (in != null) {
                            while((bytesRead = in.read(contents)) != -1) {
                                strContents.append(new String(contents, 0, bytesRead));
                            }
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error connecting to device via http - error opening stream");
                    }
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG,"Error connecting to device via http - connection failed");
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            // invio della risposta alle classi "iscritte"
            for(HttpRequestCompleted requestCompleted : httpRequestList)
            {
                if (requestCompleted != null)
                    requestCompleted.onHttpRequestCompleted(strContents.toString());
            }
            Log.i(TAG, strContents.toString());
        }).start();

    }
}
