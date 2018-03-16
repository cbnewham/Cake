package com.waracle.androidtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.waracle.androidtest.network.DownloadImageAsync;

import java.security.InvalidParameterException;
import java.util.HashMap;



public class MyImageView extends AppCompatImageView implements DownloadImageAsync.DownloadListener
{
    // This is quick and dirty. I would use a proper FIFO cache here - to disk: using preferences or a database to hold the
    // keys and storing the bitmaps as images in the filesystem. Rebuild the cache when the app is reloaded. Obviously I'd
    // use a third-party image cache to save writing my own. This would also remove the mapping from MyImageView.
    private static HashMap<String, Bitmap> imageCache = new HashMap();

    private String url;


    public MyImageView(Context context)
    {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     */
    public void loadFromURL(String url)
    {
        if (TextUtils.isEmpty(url.trim()))
        {
            throw new InvalidParameterException("URL is empty!");
        }

        this.url = url;

        DownloadImageAsync download = (DownloadImageAsync) getTag();

        if (download != null)
        {
            // Abort any previous download that may be in progress.
            download.abort();
        }

        Bitmap bitmap = imageCache.get(url);

        if (bitmap != null)
        {
            setImageBitmap(bitmap);
            setTag(null);
            return;
        }

        download = new DownloadImageAsync(this);
        setTag(download);

        download.execute(url);
    }


    @Override
    public void downloadConcluded(Bitmap bitmap)
    {
        imageCache.put(url, bitmap);
        setImageBitmap(bitmap);
        setTag(null); // Remove any Download task.
    }


    @Override
    public void downloadFailed()
    {
        setTag(null); // Remove any Download task.
    }
}
