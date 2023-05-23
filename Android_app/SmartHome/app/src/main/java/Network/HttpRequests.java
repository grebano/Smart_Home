package Network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Interfaces.HttpRequestCompleted;

public class HttpRequests {
    private final String TAG = "HttpRequest";
    private String url = "";

    private HttpRequestCompleted httpRequestCompleted = null;

    public HttpRequests(String url, HttpRequestCompleted httpRequestCompleted)
    {
        this.url = url;
        this.httpRequestCompleted = httpRequestCompleted;
    }

    public void Request(String path)
    {
        DoaRequest(this.url,path);
    }

    private void DoaRequest(String url, String path)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strContents = "";
                URL myUrl = null;
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
                            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

                            byte[] contents = new byte[1024];
                            int bytesRead = 0;
                            while((bytesRead = in.read(contents)) != -1) {
                                strContents += new String(contents, 0, bytesRead);
                            }
                            in.close();
                        } catch (IOException e) {
                            Log.e(TAG,"Error connecting to device via http");
                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Error connecting to device via http");
                }
                urlConnection.disconnect();
                httpRequestCompleted.onHttpRequestCompleted(strContents);
                Log.i(TAG,strContents);
            }
        }).start();

    }
}
