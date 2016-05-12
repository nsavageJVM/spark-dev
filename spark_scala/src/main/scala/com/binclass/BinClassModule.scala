package com.binclass

import com.io.{CsvRecord, CsvToken, ParseCsv}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by ubu on 11.05.16.
  * https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-sql-dataframe.html
  */
object BinClassModule extends SparkConfig {


   def tokensToDataFrame(): Unit = {

     val recordsPath :String =  ParseCsv.findResource("/ComercialBanks10k.csv")
     val records: Seq[CsvRecord] = ParseCsv.readFromCsvFile(recordsPath)(CsvRecord.parse)
     val tokens: Seq[CsvToken] = ParseCsv.readFromCsvRecords(records)(CsvToken.parse )
     // process header
     val headers: Seq[String] = CsvToken.getProcessTokens(tokens.head.tokens)
     val processedTokens: Seq[Seq[String]] = tokens.map(t =>  CsvToken.getProcessTokens(t.tokens) ).drop(1)
     val nonEmptyTokens =  ParseCsv.mapTonNnEmptyResults(processedTokens)

     val schema =
       StructType( headers.map(fieldName => StructField(fieldName, StringType, true)))
     import sqlContext.implicits._

     val rdd = sc.parallelize(nonEmptyTokens)
     val rowRDD = rdd.map(p => Row(p(0), p(1) , p(2)))
     val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)
//
//
//     val trainingDataSet = processedTokens.toDS()
//     val trainingDataFrame = trainingDataSet.toDF( )


     peopleDataFrame.printSchema()
     peopleDataFrame.show()


   }



}
