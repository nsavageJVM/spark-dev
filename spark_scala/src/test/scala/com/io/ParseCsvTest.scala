package com.io

import org.junit.{Before, Test}
import org.scalatest.junit.JUnitSuite

import scala.collection.mutable.ListBuffer

/**
  * http://www.scalatest.org/getting_started_with_junit_4_in_scala
  */
class ParseCsvTest  extends JUnitSuite {


  @Before def initialize() {
    println("Before" )
  }

  @Test
  def verifyResource() {
    println("finding resouce omercialBanks10k.csv"  )
    println(ParseCsv.findResource("/ComercialBanks10k.csv"))

  }

  @Test
  def obtainCSVRecords() {

    val recordsPath :String =  ParseCsv.findResource("/ComercialBanks10k.csv")
    println("found path for resouce omercialBanks10k.csv \n :: "+recordsPath   )

    val records: Seq[CsvRecord] = ParseCsv.readFromCsvFile(recordsPath)(CsvRecord.parse)

    println("found header"+records.head  )
    println("found records "+records.drop(1).take(10)  )


  }

  @Test
  def obtainCSVTokens() {

    val recordsPath :String =  ParseCsv.findResource("/ComercialBanks10k.csv")
    println("found path for resouce omercialBanks10k.csv \n :: "+recordsPath   )

    val records: Seq[CsvRecord] = ParseCsv.readFromCsvFile(recordsPath)(CsvRecord.parse)

    println("found header "+records.head  )
    println("found records "+records.drop(1).take(10)  )

    val tokens: Seq[CsvToken] = ParseCsv.readFromCsvRecords(records)(CsvToken.parse )

    println("found token headers "+tokens.head  )
    println("found tokens list "+tokens.drop(1).take(2)  )
  }


  @Test
  def obtainCSVProcessedTokens() {

    val recordsPath :String =  ParseCsv.findResource("/ComercialBanks10k.csv")
    val records: Seq[CsvRecord] = ParseCsv.readFromCsvFile(recordsPath)(CsvRecord.parse)
    val tokens: Seq[CsvToken] = ParseCsv.readFromCsvRecords(records)(CsvToken.parse )

    val header: CsvToken = tokens.head
//   WrappedArray(Id, Name, BillingStreet, BillingCity, BillingState, BillingPostalCode, BillingCountry, Phone, AccountNumber)
    println("found token headers "+header.tokens  )

    val headers: Seq[String] = CsvToken.getProcessTokens(header.tokens)

    println("process token headers ")
    headers.map(x => println(x) )
    println("process some token   ")

    val processedTokens: Seq[Seq[String]] = tokens.map(t =>  CsvToken.getProcessTokens(t.tokens) ).drop(1)

    val nonEmptyTokens =  ParseCsv.mapTonNnEmptyResults(processedTokens)

    nonEmptyTokens.foreach( pT =>   println( pT))

  }

}
