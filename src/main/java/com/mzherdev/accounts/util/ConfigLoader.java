package com.mzherdev.accounts.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigLoader {
    private static final String APPLICATION_PROPS_FILE_NAME = "/application.properties";
    private static final Properties properties = new Properties();
    private static final Logger log = Logger.getLogger(DBHelper.class);

    static {
        String configFileName = Optional.ofNullable(System.getProperty(APPLICATION_PROPS_FILE_NAME))
                .orElse(APPLICATION_PROPS_FILE_NAME);
        loadProperties(configFileName);

    }

    static String getProperty(String key) {
        return properties.getProperty(key);
    }

    private static void loadProperties(String fileName) {
        try (InputStream fis = DBHelper.class.getResourceAsStream(fileName)) {
            properties.load(fis);
        } catch (IOException ioe) {
            log.error("Error during reading the config " + fileName, ioe);
        }
    }
}
