package com.test.framework.utils;

/**
 * Constants used throughout the framework
 */
public class Constants {
    
    // Timeouts in seconds
    public static final int DEFAULT_TIMEOUT = 30;
    public static final int DEFAULT_POLLING_INTERVAL = 500; // milliseconds
    
    // URLs
    public static final String GOOGLE_URL = "https://www.google.com";
    public static final String BROWSERSTACK_DEMO_URL = "https://www.bstackdemo.com/";
    
    // Environment variables
    public static final String ENV_BROWSERSTACK_USERNAME = "BROWSERSTACK_USERNAME";
    public static final String ENV_BROWSERSTACK_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY";
    
    // Configuration keys
    public static final String CONFIG_BROWSER = "browser";
    public static final String CONFIG_HEADLESS = "headless";
    public static final String CONFIG_IMPLICIT_WAIT = "implicit.wait";
    public static final String CONFIG_EXPLICIT_WAIT = "explicit.wait";
    
    // BrowserStack specific config keys
    public static final String BS_CONFIG_USERNAME = "username";
    public static final String BS_CONFIG_ACCESS_KEY = "access_key";
    public static final String BS_CONFIG_OS = "os";
    public static final String BS_CONFIG_OS_VERSION = "os_version";
    public static final String BS_CONFIG_BROWSER_VERSION = "browser_version";
    public static final String BS_CONFIG_PROJECT = "project";
    public static final String BS_CONFIG_BUILD = "build";
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
