package com.logistic;

import com.logistic.artifacts.DuplicateBag;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * Created by ubu on 19.10.15.
 */
public class KMeansClusterRunner {


    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("KMeansClusterRunner").setMaster("local[*]");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        JavaRDD<String> addressDataWithHeader = ctx
                .textFile(KMeansClusterRunner.class.getResource("/ComercialBanks10k.csv").toExternalForm());

        JavaRDD<DuplicateBag> addressData =  getDistinctValues(addressDataWithHeader);


    }


    static JavaRDD<DuplicateBag> getDistinctValues(JavaRDD<String> addressDataWithHeader ) {

        String  header = addressDataWithHeader.first();

        addressDataWithHeader = addressDataWithHeader.filter( x-> !x.equals(header));

        JavaRDD<DuplicateBag> addressData = addressDataWithHeader
                .map( line -> {
                           String[] parts = line.split("\t");
                           return  new DuplicateBag(parts[0], parts[1], parts[2], parts[3],
                                    parts[4], parts[5], parts[6], parts[7]);
                        }
                );


        return addressData;
    }

}
