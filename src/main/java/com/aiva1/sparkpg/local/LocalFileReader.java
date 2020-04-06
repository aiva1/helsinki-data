package com.aiva1.sparkpg.local;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class LocalFileReader {

    private static final Pattern SPACE = Pattern.compile(" ");

    public LocalFileReader() {
    }

    public void executeCountOfLocalFile() {
        //Create a SparkContext to initialize
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Word Count");

        // Create a Java version of the Spark Context
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);

        // Load the text into a Spark RDD, which is a distributed representation of each line of text
        JavaRDD<String> textFile = ctx.textFile("src/main/resources/alice-in-wonderland.txt");

        //countAndPrintResultToConsole(textFile);
        countAndSaveToLocalFile(textFile, "src/main/resources/wordcount");

        ctx.stop();
    }


    /**
     * Print each word and it's amount to console
     * @param fileReadFrom path to text file to read from
     * @return total count of words found
     */
    private long countAndPrintResultToConsole(JavaRDD<String> fileReadFrom) {
        JavaRDD<String> words = fileReadFrom.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());
        JavaPairRDD<String, Integer> ones = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(Integer::sum);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

        return counts.count();
    }

    /**
     * Print total word count and save result as text file
     *
     * @param fileReadFrom path to text file to read from
     * @param fileSaveTo path to file to save the output to
     * @return total count of words found
     */
    private long countAndSaveToLocalFile(JavaRDD<String> fileReadFrom, String fileSaveTo) {
        JavaPairRDD<String, Integer> counts = fileReadFrom
                .flatMap(s -> Arrays.asList(s.split("[ ,]")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);
        counts.foreach(System.out::println);
        System.out.println("Total words: " + counts.count());
        counts.saveAsTextFile(fileSaveTo);

        return counts.count();
    }
}
