package com.logistic;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.*;

import com.google.common.collect.Lists;
import com.logistic.artifacts.*;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

// https://github.com/apache/spark/tree/master/data/mllib
// The new set of APIs will live under org.apache.spark.ml, 
// and o.a.s.mllib will be deprecated 

public class LogisticRunner {

	public static void main(String[] args) {
		
		SparkConf sparkConf = new SparkConf().setAppName("JavaSparkDevJob").setMaster("local[*]");
	    JavaSparkContext jsc = new JavaSparkContext(sparkConf);
	    
	    SQLContext jsql = new SQLContext(jsc);

	    // Prepare training documents, which are labeled.
	    List<LabeledDocument> localTraining = Lists.newArrayList(
	      new LabeledDocument(0L, "a b c d e spark", 1.0),
	      new LabeledDocument(1L, "b d", 0.0),
	      new LabeledDocument(2L, "spark f g h", 1.0),
	      new LabeledDocument(3L, "hadoop mapreduce", 0.0));
	    DataFrame training = jsql.createDataFrame(jsc.parallelize(localTraining), LabeledDocument.class);

	    // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
	    Tokenizer tokenizer = new Tokenizer()
	      .setInputCol("value")
	      .setOutputCol("words");
	    HashingTF hashingTF = new HashingTF()
	      .setNumFeatures(1000)
	      .setInputCol(tokenizer.getOutputCol())
	      .setOutputCol("features");
	    LogisticRegression lr = new LogisticRegression()
	      .setMaxIter(10)
	      .setRegParam(0.001);
	    Pipeline pipeline = new Pipeline()
	      .setStages(new PipelineStage[] {tokenizer, hashingTF, lr});

	    // Fit the pipeline to training documents.
	    PipelineModel model = pipeline.fit(training);

	    // Prepare test documents, which are unlabeled.
	    List<Document> localTest = Lists.newArrayList(
	      new Document(4L, "spark i j k"),
	      new Document(5L, "l m n"),
	      new Document(6L, "spark hadoop spark"),
	      new Document(7L, "apache hadoop"));
	    
	    DataFrame test = jsql.createDataFrame(jsc.parallelize(localTest), Document.class);

	    // Make predictions on test documents.
	    DataFrame predictions = model.transform(test);
	    for (Row r: predictions.select("id", "value", "probability", "prediction").collect()) {
	      System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> prob=" + r.get(2)
	          + ", prediction=" + r.get(3));
	    }

	    jsc.stop();
	  }


	}

