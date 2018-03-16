package com.waracle.androidtest.network;

import android.os.AsyncTask;

import com.waracle.androidtest.utils.StreamUtils;
import com.waracle.androidtest.utils.Utils;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Download JSON.
 */
public class DownloadJSONAsync extends AsyncTask<Void, Void, JSONArray>
{
    private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";


    private DownloadListener listener;


    public DownloadJSONAsync(DownloadListener listener)
    {
        this.listener = listener;
    }


    public void abort()
    {
        listener = null;
        this.cancel(true);
    }


    @Override
    protected JSONArray doInBackground(Void... params)
    {
        HttpURLConnection urlConnection = null;

        try
        {
            URL url = new URL(JSON_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            // Also, Do you trust any utils thrown your way????
            
            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK &&
                    (responseCode == HttpURLConnection.HTTP_MOVED_PERM
                            || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                            || responseCode == HttpURLConnection.HTTP_SEE_OTHER))
            {
                // Handle redirects.
                String newUrl = urlConnection.getHeaderField("Location");
                urlConnection.disconnect();
                urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
                inputStream = urlConnection.getInputStream();
            }

            byte[] data = StreamUtils.readByteArrayFromStream(inputStream);

            // Read in charset of HTTP content.
            String charset = Utils.parseCharset(urlConnection.getRequestProperty("Content-Type"));

            // Convert byte array to appropriate encoded string.
            String jsonText = new String(data, charset);

            // Read string as JSON.
            return new JSONArray(jsonText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            listener.downloadFailed();
            listener = null;
        }
        finally
        {
            urlConnection.disconnect();
        }

        return new JSONArray();
    }


    @Override
    protected void onPostExecute(JSONArray array)
    {
        if (listener != null)
        {
            listener.downloadConcluded(array);
        }

        listener = null;
    }


    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        listener = null;
    }


    public interface DownloadListener
    {
        void downloadConcluded(JSONArray array);

        void downloadFailed();
    }
}
