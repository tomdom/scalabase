package com.github.tomdom.scalabase.io

import java.io.File

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ FlatSpec, Matchers }

class IoSpec extends FlatSpec with MockitoSugar with Matchers {
  "FileDuplications" should "return mocked List of Files in mocked sub directories" in {
    val file = mock[File]
    when(file.isDirectory) thenReturn false
    when(file.isFile) thenReturn true

    val dir = mock[File]
    when(dir.isDirectory) thenReturn true
    when(dir.isFile) thenReturn false
    when(dir.listFiles()) thenReturn (Array[File]())

    val root = mock[File]
    when(root.isDirectory) thenReturn true
    when(root.isFile) thenReturn false
    when(root.listFiles()) thenReturn (Array(dir, file))

    val fs = files(root)

    fs.length should be(1)
    fs.foreach(f => println(f.getPath))
  }
}
