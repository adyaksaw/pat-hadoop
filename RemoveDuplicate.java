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

public class RemoveDuplicate {
    public static class RemoveDuplicateMapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            context.write(new Text(line[0]), new Text(line[1]));
        }
    }

    public static class RemoveDuplicateReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text t_key, Iterator<Text> values, Context context) throws IOException, InterruptedException {
            while (values.hasNext()) {
                Text value = values.next();
                if(t_key.toString().compareTo(value.toString()) < 0){
                    context.write(t_key, value);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Bidirected Edge Generation");
        job.setJarByClass(RemoveDuplicate.class);
        job.setMapperClass(RemoveDuplicateMapper.class);
        job.setReducerClass(RemoveDuplicateReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}