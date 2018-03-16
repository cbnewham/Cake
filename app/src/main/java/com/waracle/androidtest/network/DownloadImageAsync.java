package com.waracle.androidtest.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.waracle.androidtest.utils.StreamUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Download images.
 *
 */
public class DownloadImageAsync extends AsyncTask<String, Void, Bitmap>
{
    private DownloadListener listener;

    public DownloadImageAsync(DownloadListener listener)
    {
        this.listener = listener;
    }


    public void abort()
    {
        listener = null;
        this.cancel(true);
    }


    @Override
    protected Bitmap doInBackground(String... url)
    {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        byte[] data = null;

        try
        {
            urlConnection = (HttpURLConnection) new URL(url[0]).openConnection();
            // Read data from workstation
            inputStream = urlConnection.getInputStream();

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

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

            data = StreamUtils.readByteArrayFromStream(inputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            listener.downloadFailed();
            listener = null;
        }
        finally
        {
            StreamUtils.close(inputStream);
            urlConnection.disconnect();
        }

        if (data != null)
        {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        else
        {
            // Create an "empty" bitmap. Don't return nulls.
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            return Bitmap.createBitmap(0, 0, conf);
        }
    }


    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (listener != null)
        {
            listener.downloadConcluded(bitmap);
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
        void downloadConcluded(Bitmap bitmap);

        void downloadFailed();
    }
}
