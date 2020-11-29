# pat-hadoop

## Preprocess1
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess1.java
jar cf Preprocess1.jar Preprocess1*.class
$HADOOP_HOME/bin/hadoop jar Preprocess1.jar Preprocess1 /Data /Preprocessed1
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Preprocessed1
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess1.java
jar cf Preprocess1.jar Preprocess1*.class
$HADOOP_HOME/bin/hadoop jar Preprocess1.jar Preprocess1 /Data /Preprocessed1
$HADOOP_HOME/bin/hadoop fs -cat /Preprocessed1/part-r-00000
```

## Preprocess2
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess2.java
jar cf Preprocess2.jar Preprocess2*.class
$HADOOP_HOME/bin/hadoop jar Preprocess2.jar Preprocess2 /Preprocessed1 /Preprocessed2
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Preprocessed2
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess2.java
jar cf Preprocess2.jar Preprocess2*.class
$HADOOP_HOME/bin/hadoop jar Preprocess2.jar Preprocess2 /Preprocessed1 /Preprocessed2
$HADOOP_HOME/bin/hadoop fs -cat /Preprocessed2/part-r-00000
```

## Preprocess3
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess3.java
jar cf Preprocess3.jar Preprocess3*.class
$HADOOP_HOME/bin/hadoop jar Preprocess3.jar Preprocess3 /Preprocessed1 /Preprocessed2 /Preprocessed3
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Preprocessed3
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess3.java
jar cf Preprocess3.jar Preprocess3*.class
$HADOOP_HOME/bin/hadoop jar Preprocess3.jar Preprocess3 /Preprocessed1 /Preprocessed2 /Preprocessed3
$HADOOP_HOME/bin/hadoop fs -cat /Preprocessed3/part-r-00000
```

## Preprocess4
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess4.java
jar cf Preprocess4.jar Preprocess4*.class
$HADOOP_HOME/bin/hadoop jar Preprocess4.jar Preprocess4 /Preprocessed2 /Preprocessed3 /Preprocessed4
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Preprocessed4
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main Preprocess4.java
jar cf Preprocess4.jar Preprocess4*.class
$HADOOP_HOME/bin/hadoop jar Preprocess4.jar Preprocess4 /Preprocessed2 /Preprocessed3 /Preprocessed4
$HADOOP_HOME/bin/hadoop fs -cat /Preprocessed4/part-r-00000
```

## Fase 1
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Fase1Output
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main TriangleGeneration.java
jar cf TriangleGeneration.jar TriangleGeneration*.class
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Fase1Output
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main TriangleGeneration.java
jar cf TriangleGeneration.jar TriangleGeneration*.class
$HADOOP_HOME/bin/hadoop jar TriangleGeneration.jar TriangleGeneration /PreprocessedData /Fase1Output
$HADOOP_HOME/bin/hadoop fs -cat /Fase1Output/part-r-00000

```

## Fase 2
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main TriangleCalculation.java
jar cf TriangleCalculation.jar TriangleCalculation*.class
$HADOOP_HOME/bin/hadoop jar TriangleCalculation.jar TriangleCalculation /PreprocessedData /Fase1Output /Fase2Output
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Fase2Output
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main TriangleCalculation.java
jar cf TriangleCalculation.jar TriangleCalculation*.class
$HADOOP_HOME/bin/hadoop jar TriangleCalculation.jar TriangleCalculation /PreprocessedData /Fase1Output /Fase2Output
$HADOOP_HOME/bin/hadoop fs -cat /Fase2Output/part-r-00000
```

## Command berguna lainnya:
```
Copy:
$HADOOP_HOME/bin/hdfs dfs -copyFromLocal ./Data /

ls:
$HADOOP_HOME/bin/hadoop fs -ls /Fase1Output

Remove:
$HADOOP_HOME/bin/hadoop fs -rm -r -f /Fase1Output

Cat:
$HADOOP_HOME/bin/hadoop fs -cat /Fase1Output/part-r-00000
```