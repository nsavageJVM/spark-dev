package com.logistic;

import com.logistic.artifacts.DuplicateBag;
import com.logistic.artifacts.KeyValueBag;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.util.StringJoiner;


/**
 * http://spark.apache.org/docs/latest/mllib-clustering.html
 */
public class KMeansClusterRunner {


    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("KMeansClusterRunner").setMaster("local[*]");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        JavaRDD<String> addressDataWithHeader = ctx
                .textFile(KMeansClusterRunner.class.getResource("/ComercialBanks10k.csv").toExternalForm());

        JavaRDD<KeyValueBag> addressData =  getReducedValues(addressDataWithHeader);

//        addressData.foreach(
//               kvBag ->    System.out.println(kvBag.value() )
//        );

        JavaRDD<Vector> parsedDataL = addressData.map(
                kVBag -> {
                    String[] sarray = kVBag.value().trim().split(" ");
                    double[] values = new double[1];
                        values[0] = sarray[0].hashCode();
                    return Vectors.dense(values);
                }
        );

        parsedDataL.foreach(
               kvBag ->    System.out.println(kvBag.toArray().length )
        );


        parsedDataL.cache();

        // Cluster the data into two classes using KMeans
        int numClusters = 2;
        int numIterations = 20;
        // clustering is unsupervised so no labels
        KMeansModel clusters = KMeans.train(parsedDataL.rdd(), numClusters, numIterations);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(parsedDataL.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        // Save and load model
        clusters.save(ctx.sc(), "myModelPath");
        KMeansModel sameModel = KMeansModel.load(ctx.sc(), "myModelPath");


    }


    static JavaRDD<KeyValueBag> getReducedValues(JavaRDD<String> addressDataWithHeader ) {

        String  header = addressDataWithHeader.first();

        addressDataWithHeader = addressDataWithHeader.filter( x-> !x.equals(header));

        JavaRDD<KeyValueBag> addressData = addressDataWithHeader
                .map( line -> {
                            StringJoiner stringJoiner = new StringJoiner(" ");
                            String[] parts = line.split("\t");
                            if(parts.length > 6) {
                                String valueString = stringJoiner
                                        .add(parts[1]).add(parts[2]).add(parts[3])
                                        .add(parts[4]).add(parts[5]).add(parts[6]).toString();
                                return  new KeyValueBag(parts[0],valueString);
                            }else {
                                String valueString = stringJoiner
                                        .add(parts[1]).toString();
                                return  new KeyValueBag(parts[0], valueString);
                            }

                        }
                );

        return addressData;
    }

}
