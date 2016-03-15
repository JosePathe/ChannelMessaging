package berthet.johann.channelmessaging.network;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Johann on 02/02/2016.
 */
public class WSRequestHandler extends AsyncTask<String,Integer,String> {
    private final int requestCode;
    private ArrayList<onWSRequestListener> listeners = new ArrayList<onWSRequestListener>();
    private HashMap<String, String> hashMapParams;
    String requestURL = null;

    public WSRequestHandler(HashMap<String, String> hashMapParams, String requestURL, int requestCode) {
        this.hashMapParams = hashMapParams;
        this.requestURL = requestURL;
        this.requestCode = requestCode;
    }

    public void setOnWSRequestListener(onWSRequestListener listener) {
        this.listeners.add(listener);
    }

    private void WSRequestCompleted(String response) {
        for (onWSRequestListener myListener:listeners) {
            myListener.onWSRequestCompleted(requestCode, response);
        }
    }

    private void WSRequestError(String exception) {
        for (onWSRequestListener myListener:listeners) {
            myListener.onWSRequestError(requestCode, exception);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Gson gson = new Gson();
        String response = "";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(hashMapParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            } else {
                response="";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        WSRequestCompleted(response);
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
