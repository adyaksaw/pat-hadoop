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

public class TriangleCalculation {
    public static class TriangleCalculationMapper extends Mapper <Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] line = valueString.split("\t");
            if(line.length == 2){
                context.write(new Text(line[0] + "\t" + line[1]), new Text("$"));
            } else if(line.length == 3){
                context.write(new Text(line[1] + "\t" + line[2]), new Text(line[0]));
            } else{
                throw new IOException("Ada file dengan format salah");
            }
        }
    }

    public static class TriangleCalculationReducer extends Reducer<Text, Text, Text, IntWritable> {
        private int total;
        private IntWritable result = new IntWritable();

        public void setup(Context context) throws IOException, InterruptedException {
            total = 0;
        }

        public void reduce(Text t_key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> list = new ArrayList<String>();
            for(Text t: values){
                list.add(t.toString());
            }
            list = new ArrayList<>(new HashSet<>(list));
            if(list.get(0).equals("$")){
                total += list.size()-1;
            }
        }

        public void cleanup(Context context) throws IOException, InterruptedException {
            result.set(total);
            context.write(new Text("Total"), result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Triangle Calculation");
        job.setJarByClass(TriangleCalculation.class);
        job.setMapperClass(TriangleCalculationMapper.class);
        job.setReducerClass(TriangleCalculationReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}