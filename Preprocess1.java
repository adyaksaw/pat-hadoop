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

public class Preprocess1 {
    public static class Preprocess1Mapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            context.write(new Text(line[0]), new Text(line[1]));
            context.write(new Text(line[1]), new Text(line[0]));
        }
    }

    public static class Preprocess1Reducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text t_key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> list = new ArrayList<String>();
            for(Text t: values){
                list.add(t.toString());
            }
            list = new ArrayList<>(new HashSet<>(list));

            for(int i = 0; i < list.size(); i++){
                if(t_key.toString().compareTo(list.get(i)) < 0){
                    context.write(new Text(t_key.toString()), new Text(list.get(i)));
                    context.write(new Text(list.get(i)), new Text(t_key.toString()));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Preprocess Graph");
        job.setJarByClass(Preprocess1.class);
        job.setMapperClass(Preprocess1Mapper.class);
        job.setReducerClass(Preprocess1Reducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}