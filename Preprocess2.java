import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Preprocess2 {
    public static class Preprocess2Mapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            context.write(new Text(line[0]), new Text("1"));
            context.write(new Text(line[1]), new Text("1"));
        }
    }

    public static class Preprocess2Reducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text t_key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int degree = 0;
            for (Text val: values) {
                degree -= 1;
            }
            context.write(t_key, new Text(String.valueOf(degree)));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Preprocess Degree");
        job.setJarByClass(Preprocess2.class);
        job.setMapperClass(Preprocess2Mapper.class);
        job.setReducerClass(Preprocess2Reducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}