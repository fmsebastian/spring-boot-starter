package com.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigLoader load a property file into java {@link java.util.Properties} object.
 * @author fsebastian
 */
public class ConfigLoader {

    private ConfigLoader (){
        throw new IllegalStateException("This class should not be instantiated");
    }

    /**
     * loadConfig uses a {@link java.io.FileReader} FileReader object to load the property file into a Properties object.
     *
     * @param filename of the file (including path).
     * @return Properties object with a map of properties in @param file.
     * @throws FileNotFoundException FileReader may throw it.
     * @throws IOException Properties' load method and FileReader may throw it.
     */
    public static Properties loadConfig(String filename) throws FileNotFoundException, IOException{
        Properties props = new Properties();
        props.load(new FileReader(filename));
        return props;
    }
}