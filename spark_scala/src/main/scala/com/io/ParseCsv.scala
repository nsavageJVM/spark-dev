package com.io

import scala.io.Source
import scala.util.{Success, Try}


case class CsvRecord(line: String)

object CsvRecord {

  def parse(line: String): CsvRecord = {

    CsvRecord(line)
  }

}



object ParseCsv {

  def findResource(res: String):  String = {

    classOf[CsvRecord].getResource("/ComercialBanks10k.csv").getPath

  }

  def readFromCsvFile[T](path: String)(implicit parse: String => T) : Seq[T] = {

    val lines = Source.fromFile(path).getLines().toSeq

    val validLines = Try(parse(lines.head)) match {
      case Success(_) => lines
      case _ => lines.tail
    }
    validLines.map(parse)

  }



}
