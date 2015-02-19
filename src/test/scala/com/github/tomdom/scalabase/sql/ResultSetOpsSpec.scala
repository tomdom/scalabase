package com.github.tomdom.scalabase.sql

import java.sql.{ SQLException, ResultSet }

import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import org.scalatest.mock.MockitoSugar
import org.scalatest.{ FlatSpec, Matchers }

class ResultSetOpsSpec extends FlatSpec with MockitoSugar with Matchers {
  import com.github.tomdom.scalabase.Helper._
  import ResultSetOps._

  "ResultSet" should "be implicitly converted to Stream" in {
    val rs = mock[ResultSet]
    when(rs.next()) thenReturn true thenReturn true thenReturn true thenReturn false
    when(rs.getString(1)) thenReturn "row_1" thenReturn "row_2" thenReturn "row_3" thenThrow new SQLException

    implicit def row(rs: ResultSet) = rs.getString(1)

    using(rs) { rs =>
      rs.toStream
      verify(rs, never).getString(1)
    }
  }

  it should "only read first ResultSet row while initializing val" in {
    val rs = mock[ResultSet]
    when(rs.next()) thenReturn true thenReturn true thenReturn true thenReturn false
    when(rs.getString(1)) thenReturn "row_1" thenReturn "row_2" thenReturn "row_3" thenThrow new SQLException

    implicit def row(rs: ResultSet) = rs.getString(1)

    using(rs) { rs =>
      val s: Stream[String] = rs.toStream
      verify(rs).getString(1)
    }
  }

  it should "read all ResultSet rows when asked for its length" in {
    val rs = mock[ResultSet]
    when(rs.next()) thenReturn true thenReturn true thenReturn true thenReturn false
    when(rs.getString(1)) thenReturn "row_1" thenReturn "row_2" thenReturn "row_3" thenThrow new SQLException

    implicit def row(rs: ResultSet) = rs.getString(1)

    using(rs) { rs =>
      val s: Stream[String] = rs.toStream
      s.length shouldBe 3
      verify(rs, times(3)).getString(1)
    }
  }

  it should "read one ResultSet row after the other when mapped" in {
    val rs = mock[ResultSet]
    when(rs.next()) thenReturn true thenReturn true thenReturn true thenReturn false
    when(rs.getString(1)) thenReturn "row_1" thenReturn "row_2" thenReturn "row_3" thenThrow new SQLException

    implicit def row(rs: ResultSet) = rs.getString(1)

    using(rs) { rs =>
      val s: Stream[String] = rs.toStream

      val swi = s.zipWithIndex.map(t => t._1 -> (t._2 + 1))
      verify(rs, times(1)).getString(1)

      for (rwi <- swi) {
        rwi._1 shouldBe "row_" + rwi._2
        verify(rs, times(rwi._2)).getString(1)
      }

      verify(rs, times(3)).getString(1)
    }
  }
}
