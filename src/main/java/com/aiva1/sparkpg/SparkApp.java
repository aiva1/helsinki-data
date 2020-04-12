/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.aiva1.sparkpg;

import com.aiva1.sparkpg.database.FromDatabaseToCSVTransformer;

public class SparkApp {

    public String getGreeting() {
        return "Hello world.";
    }

        public static void log(Object... args) {
            for (Object elem : args) {
                System.out.println(elem.toString());
            }
        }

    public static void main(String[] args) {
        SparkApp leApp = new SparkApp();
        System.out.println(leApp.getGreeting());

        FromDatabaseToCSVTransformer toCsv = new FromDatabaseToCSVTransformer();
        boolean isSuccess = toCsv.executeNonUsSummaryTransformation();

        log(isSuccess);

    }
}
