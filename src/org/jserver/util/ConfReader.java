package org.jserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yuantao on 2014/6/22.
 */
public class ConfReader {
    private static Properties properties = null;

    static{
        InputStream inputStream = ConfReader.class.getResourceAsStream("/conf.ini");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }

}
