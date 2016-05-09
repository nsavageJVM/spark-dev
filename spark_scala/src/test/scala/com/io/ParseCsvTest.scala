package com.io

import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitSuite

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

}
