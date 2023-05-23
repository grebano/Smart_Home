package Network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Interfaces.HttpRequestCompleted;

public class HttpRequests {
    private final String TAG = "HttpRequest";
    private String url = "";

    // TODO se altre classi vogliono i risultati devo aggiungere gli oggetti
    // oggetti dell'interfaccia usata per restituire informazioni (http response)

    ArrayList<HttpRequestCompleted> httpRequestList = null;

    // costruttore oggetto della classe
    public HttpRequests(String url, ArrayList<HttpRequestCompleted> givenhttpRequestList)
    {
        this.url = url;
        httpRequestList = new ArrayList<HttpRequestCompleted>(givenhttpRequestList);
    }

    // wrapper della funzione DoaRequest, che passa l'url corretto
    public void Request(String path)
    {
        DoaRequest(this.url,path);
    }


    // funzione che gestisce le richieste http ad uno specifico url e path
    private void DoaRequest(String url, String path)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strContents = "";
                URL myUrl = null;
                // creazione dell'url completo
                try {
                    myUrl = new URL(url + path);
                } catch (MalformedURLException e) {
                    Log.e(TAG,"Error connecting to device via http - bad url");
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) myUrl.openConnection();
                    try {
                        try {

                            // apertura e lettura dallo stream http
                            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

                            byte[] contents = new byte[1024];
                            int bytesRead = 0;
                            while((bytesRead = in.read(contents)) != -1) {
                                strContents += new String(contents, 0, bytesRead);
                            }
                            in.close();
                        } catch (IOException e) {
                            Log.e(TAG,"Error connecting to device via http - error opening stream");
                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Error connecting to device via http - connection failed");
                }
                urlConnection.disconnect();

                // invio della risposta alle classi "iscritte"
                for(HttpRequestCompleted requestCompleted : httpRequestList)
                {
                    if (requestCompleted != null)
                        requestCompleted.onHttpRequestCompleted(strContents);
                }
                Log.i(TAG,strContents);
            }
        }).start();

    }
}
