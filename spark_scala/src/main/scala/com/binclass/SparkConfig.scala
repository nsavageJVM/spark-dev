package com.binclass

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by ubu on 11.05.16.
  */
trait SparkConfig {

  val sparkConf: SparkConf = new SparkConf()
                                  .setAppName("BinClassRunner")
                                  .setMaster("local[*]")

  val sc: SparkContext = new SparkContext(sparkConf)

  val sqlContext: SQLContext = new SQLContext(sc)

}
