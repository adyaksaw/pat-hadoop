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

public class TriangleGeneration {
    public static class TriangleGenerationMapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            if(Integer.valueOf(line[2]) < Integer.valueOf(line[3])){
                context.write(new Text(line[0]), new Text(line[1]));
            }
        }
    }

    public static class TriangleGenerationReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text t_key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> list = new ArrayList<String>();
            for(Text t: values){
                list.add(t.toString());
            }
            list = new ArrayList<>(new HashSet<>(list));

            for(int i = 0; i < list.size(); i++){
                for(int j = i+1; j < list.size(); j++){
                    context.write(new Text(t_key.toString()), new Text(list.get(i) + "\t" + list.get(j)));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Triangle Generation");
        job.setJarByClass(TriangleGeneration.class);
        job.setMapperClass(TriangleGenerationMapper.class);
        job.setReducerClass(TriangleGenerationReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}