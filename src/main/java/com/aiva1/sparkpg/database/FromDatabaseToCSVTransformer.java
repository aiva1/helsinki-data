package com.aiva1.sparkpg.database;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.collection.immutable.Map;

import java.io.IOException;

public class FromDatabaseToCSVTransformer {

    public static void log(Object... args) {
        for (Object elem : args) {
            System.out.println(elem.toString());
        }
    }

    public boolean executeNonUsSummaryTransformation() {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("sparkSession.some.config.option", "some-value")
                .master("local")
                .getOrCreate();

        Map<String, String> allProperties = sparkSession.conf().getAll();

        log("spark session created OK");

        // load data from a JDBC source
        Dataset<Row> jdbcDataset = sparkSession.read()
                .format("jdbc")
                .option("driver", "org.postgresql.Driver")
                .option("url", "jdbc:postgresql://localhost:5432/postgres")
                .option("dbtable", "reports")
                .option("user", "postgres")
                .option("password", "docker")
                .load();

        long countNonUs = jdbcDataset.filter("country_region != 'US'").count();
        long countTotal = jdbcDataset.count();
        log(String.format("Total entries= %s, non-US= %s", countTotal, countNonUs));

        jdbcDataset.filter("country_region != 'US'")
                .select("country_region", "confirmed", "deaths")
                //.coalesce(1)/.repartition(1)
                .write()
                .option("header", "true")
                .csv("/Users/Shared/spark-temp/");

        try {
            // rename part file (using hadoop api) as it's not possible to save with desired name from dataset
            FileSystem fileSystem = FileSystem.get(sparkSession.sparkContext().hadoopConfiguration());
            String fileName = fileSystem.globStatus(new Path("/Users/Shared/spark-temp/part*"))[0]
                    .getPath().getName();

            fileSystem.rename(new Path("/Users/Shared/spark-temp/" + fileName),
                    new Path("/Users/Shared/spark-output/result.csv"));

            fileSystem.delete(new Path("/Users/Shared/spark-temp/"), true);
        } catch (IOException e) {
            log("Failed to save to local .csv file", e.getMessage());
            return false;
        }

        log("fin");
        return true;
    }
}
