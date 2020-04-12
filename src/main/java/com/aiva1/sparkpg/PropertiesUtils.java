package com.aiva1.sparkpg;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties readPropertiesFile(String fileName) {
        Properties properties = null;
        try (FileInputStream inputStream = new FileInputStream(fileName)){
            properties = new Properties();
            properties.load(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
