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

public class TriangleCount {
    public static class TriangleCountMapper extends Mapper <Object, Text, Text, IntWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            String triangleCountStr = line[1];

            Integer triangleCount = new Integer(Integer.parseInt(triangleCountStr));
            IntWritable triangleCountWritable = new IntWritable(triangleCount);
            context.write(new Text("Triangle Count: "), triangleCountWritable);
        }
    }

    public static class TriangleCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable totalTriangleCount = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int triangleCount = 0;
            for (IntWritable val: values) {
                triangleCount += val.get();
            }
            totalTriangleCount.set(triangleCount);
            context.write(key, totalTriangleCount);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Triangle Count");
        job.setJarByClass(TriangleCount.class);
        job.setMapperClass(TriangleCountMapper.class);
        job.setReducerClass(TriangleCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}