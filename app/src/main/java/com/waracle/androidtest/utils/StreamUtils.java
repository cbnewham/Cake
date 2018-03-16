package com.waracle.androidtest.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils
{
    private static final String TAG = StreamUtils.class.getSimpleName();


    public static byte[] readByteArrayFromStream(InputStream stream) throws IOException
    {
        // Could also do this using a ByteArrayOutputStream which may be more efficient.

        // Read in stream of bytes
        ArrayList<Byte> data = new ArrayList<>();

        int result;

        while ((result = stream.read()) != -1)
        {
            data.add((byte) result);
        }

        // Convert ArrayList<Byte> to byte[]
        byte[] bytes = new byte[data.size()];

        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = data.get(i);
        }

        // Return the raw byte array.
        return bytes;
    }


    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
