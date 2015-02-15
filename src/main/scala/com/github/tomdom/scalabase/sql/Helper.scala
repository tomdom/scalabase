package com.github.tomdom.scalabase.sql

import java.sql.ResultSet

import com.github.tomdom.scalabase.Helper._

import scala.annotation.tailrec

object Helper {
	def streamResultSet[A](rs: ResultSet)(row: ResultSet => A): Stream[A] =
		using(rs) { rs =>
			@tailrec
			def loop(acc: Stream[A]): Stream[A] = {
				if (rs.next())
					loop(row(rs) #:: acc)
				else
					acc.reverse
			}

			loop(Stream.empty)
		}
}
