# pat-hadoop

## Preprocess
```
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main PreprocessGraph.java
jar cf PreprocessGraph.jar PreprocessGraph*.class
$HADOOP_HOME/bin/hadoop jar PreprocessGraph.jar PreprocessGraph /Data /PreprocessedData
```
Debugging:
```
$HADOOP_HOME/bin/hadoop fs -rm -r -f /PreprocessedData
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main PreprocessGraph.java
jar cf PreprocessGraph.jar PreprocessGraph*.class
$HADOOP_HOME/bin/hadoop jar PreprocessGraph.jar PreprocessGraph /Data /PreprocessedData
$HADOOP_HOME/bin/hadoop fs -cat /PreprocessedData/part-r-00000
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