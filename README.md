# helsinki-data
Playground for doing some weird tnings-n-stuff using Apache Spark (Java).
Initial aim of the project was to play around with data given during JunctionX @Budapest 2019.
Seems the project will include a bit more than that as this is a playground.

## Running Locally

For development you may want to run the Spark application locally. This requires the spark 
executable to be installed. At the moment of writing, I am using v2.4.4. Check build.gradle 
for the actual version :)

### Installing Spark

On a Mac, you can install the Apache Spark CLI with Homebrew:

```bash
brew install apache-spark
```

### Running the Spark Job
Build the spark application jar using gradle

```bash
./gradlew clean build shadowJar
```

Submit the jar to Spark to start the job
```bash
spark-submit \
    --class com.aiva1.sparkpg.SparkApp \
    build/libs/helsinki-data-0.0.1-deps.jar 
```

Use this command to manually specify the driver host
```bash
spark-submit \
    --conf spark.driver.host=127.0.0.1 \
    --class com.aiva1.sparkpg.SparkApp \
    gradleBuild/libs/helsinki-data-0.0.1-deps.jar 
```

