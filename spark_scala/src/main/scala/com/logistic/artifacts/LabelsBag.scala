package com.logistic.artifacts

import scala.beans.BeanInfo

@BeanInfo
case class LabeledDocument(id: Long, text: String, label: Double)  {
  override def equals(that: Any): Boolean = ???
}

@BeanInfo
case class Document(id: Long, text: String)  {
  override def equals(that: Any): Boolean = ???
}


@BeanInfo
case class DuplicateBag(id: String, name: String, billingCity: String, billingState: String, billingPostalCode: String,
                        billingCountry: String, phone: String, accountNumber: String )  {
  override def equals(that: Any): Boolean = ???
}


@BeanInfo
case class LabeledKeyValueBag(id: Long, value: String, label: Double)  {
  override def equals(that: Any): Boolean = ???
}


@BeanInfo
case class KeyValueBag(key: String, value: String )  {
  override def equals(that: Any): Boolean = ???
}