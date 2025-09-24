package com.example.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DocumentSimilarityDriver {
    
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: DocumentSimilarityDriver <input path> <output path>");
            System.exit(-1);
        }
        
        Configuration conf = new Configuration();
        
        // Job 1: Extract word sets for each document
        Job job1 = Job.getInstance(conf, "document word extraction");
        job1.setJarByFile(DocumentSimilarityDriver.class);
        job1.setMapperClass(DocumentSimilarityMapper.class);
        job1.setReducerClass(DocumentSimilarityReducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job1, new Path(args[0]));
        Path intermediateOutput = new Path("intermediate_output");
        FileOutputFormat.setOutputPath(job1, intermediateOutput);
        
        if (!job1.waitForCompletion(true)) {
            System.exit(1);
        }
        
        System.out.println("Job 1 completed successfully!");
        System.exit(0);
    }
}