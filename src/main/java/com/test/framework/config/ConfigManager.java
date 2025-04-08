package com.test.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager to handle property files and environment variables
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties config = new Properties();
    private static final Properties browserStackConfig = new Properties();
    private static boolean initialized = false;

    /**
     * Loads both configuration files into memory
     */
    public static void init() {
        if (!initialized) {
            loadConfigFile("config.properties", config);
            loadConfigFile("browserstack.properties", browserStackConfig);
            initialized = true;
        }
    }

    /**
     * Loads a specific property file
     * 
     * @param fileName the property file to load
     * @param props the Properties object to load into
     */
    private static void loadConfigFile(String fileName, Properties props) {
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                props.load(inputStream);
                logger.info("Successfully loaded properties from {}", fileName);
            } else {
                logger.warn("Could not find property file: {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Error reading property file: {}", fileName, e);
        }
    }

    /**
     * Gets a property value from the configuration, supporting environment variable override
     * 
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        if (!initialized) {
            init();
        }
        
        // Check for environment variable first
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        
        // Check for system property next
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isEmpty()) {
            return sysValue;
        }
        
        // Lastly, check the properties file
        return config.getProperty(key);
    }

    /**
     * Gets a BrowserStack specific property
     * 
     * @param key the property key
     * @return the property value
     */
    public static String getBrowserStackProperty(String key) {
        if (!initialized) {
            init();
        }
        
        // Check for environment variable first (with BROWSERSTACK_ prefix)
        String envKey = "BROWSERSTACK_" + key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        
        // Check for system property next
        String sysKey = "browserstack." + key;
        String sysValue = System.getProperty(sysKey);
        if (sysValue != null && !sysValue.isEmpty()) {
            return sysValue;
        }
        
        // Lastly, check the browserstack properties file
        return browserStackConfig.getProperty(key);
    }
}
