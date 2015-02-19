package com.github.tomdom.scalabase.sql

import java.sql.ResultSet

import com.github.tomdom.scalabase.Helper._

import scala.annotation.tailrec

class ResultSetOps(rs: ResultSet) {
  def toStream[A](implicit toStreamElement: ResultSet => A): Stream[A] =
    if (rs.next())
      Stream.cons(toStreamElement(rs), toStream)
    else Stream.Empty
}

object ResultSetOps {
  implicit def apply(rs: ResultSet): ResultSetOps = new ResultSetOps(rs)
}
