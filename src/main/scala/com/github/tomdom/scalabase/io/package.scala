package com.github.tomdom.scalabase

import java.io.File

import scala.annotation.tailrec

package object io {
  def files(root: File): List[File] = {
    @tailrec
    def loop(dirs: List[File], acc: List[File]): List[File] = {
      if (dirs.isEmpty) acc
      else {
        val d = dirs.head

        val all = d.listFiles()
        val ds = for {
          f <- all
          if (f.isDirectory)
        } yield f

        val fs = for {
          f <- all
          if (f.isFile)
        } yield f

        loop(ds.toList ::: dirs.tail, fs.toList ::: acc)
      }
    }

    loop(root :: Nil, Nil)
  }
}
