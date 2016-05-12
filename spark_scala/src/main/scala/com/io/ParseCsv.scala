package com.io

import scala.collection.generic.SeqFactory
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Success, Try}


case class CsvRecord(line: String)

object CsvRecord {
  def parse(line: String): CsvRecord = {
    CsvRecord(line)
  }
}

case class CsvToken(tokens: Seq[String])
object CsvToken {

  def parse(line: CsvRecord ): CsvToken = {
    val splitted = line.line.split("\t")
    CsvToken(splitted)
  }

  def getProcessTokens(tokens:  Seq[String]):  Seq[String] = {
    tokens match {
      case _ if(tokens.length > 5)  =>  List(tokens(1), tokens(3) , tokens(5))
      case _ if(tokens.length <= 5) => Nil
    }

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


  def readFromCsvRecords[T](records: Seq[CsvRecord])(implicit parse: CsvRecord  => T) : Seq[T] = {

    val validRecords = Try(parse(records.head)) match {
      case Success(_) => records
      case _ => records.tail
    }
    records.map(parse)

  }


  def mapTonNnEmptyResults[T](processedTokens: Seq[Seq[String]] ) : List[ListBuffer[String]]= {

    val nonEmptyTokens   = new ListBuffer[ListBuffer[String]]

    processedTokens.foreach( pT => { if (!pT.isEmpty) {

      val toks = new ListBuffer[String]

      pT.foreach( t => (toks += t) )

      nonEmptyTokens += toks
    }
    })

    nonEmptyTokens.toList
  }

}
