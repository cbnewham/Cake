package com.waracle.androidtest.utils;

/**
 * Created by cbnewham on 14/03/2018.
 */
public class Utils
{
    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType)
    {
        if (contentType != null)
        {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++)
            {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2)
                {
                    if (pair[0].equals("charset"))
                    {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }
}
