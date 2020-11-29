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

public class Preprocess4 {
    public static class Preprocess4Mapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            if(line.length == 2){
                context.write(new Text(line[0]), new Text(line[1]));
            } else if(line.length == 3){
                context.write(new Text(line[1]), new Text(line[0] + "\t" + line[2]));
            }
        }
    }

    public static class Preprocess4Reducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text t_key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> list = new ArrayList<String>();
            Integer deg = new Integer(0);
            for(Text t: values){
                String temp = t.toString();
                if(temp.split("\t").length == 1){
                    deg = Integer.valueOf(temp);
                    deg = -deg;
                } else {
                    list.add(temp);
                }
            }

            for(int i = 0; i < list.size(); i++){
                String[] val = list.get(i).split("\t");
                context.write(new Text(t_key.toString() + "\t" + val[0]), new Text(deg.toString() + "\t" + val[1]));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Preprocess4");
        job.setJarByClass(Preprocess4.class);
        job.setMapperClass(Preprocess4Mapper.class);
        job.setReducerClass(Preprocess4Reducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}