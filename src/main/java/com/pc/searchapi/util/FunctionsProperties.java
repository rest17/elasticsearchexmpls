package com.pc.searchapi.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */
public class FunctionsProperties {

        // private static final ResourceBundle properties =
        // ResourceBundle.getBundle("pfms.properties");
        private static final Properties properties = new Properties();

        static {

            try(InputStream is = FunctionsProperties.class.getResourceAsStream("/application.properties");) {
                properties.load(is);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static String getProperty(String key) {
            // return System.getenv(key) != null ? System.getenv(key) :
            // properties.getString(key);
            return System.getenv(key) != null ? System.getenv(key) : properties.getProperty(key);
        }

        public static String getProperty(String key, String defaultVal) {
            // return System.getenv(key) != null ? System.getenv(key) :
            // properties.getString(key);
            return System.getenv(key) != null ? System.getenv(key)
                    : properties.getProperty(key) != null ? properties.getProperty(key) : defaultVal;
        }

    }
