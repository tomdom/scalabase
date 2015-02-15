package com.github.tomdom.scalabase

object Helper {
	def using[A <: {def close() : Unit}, B](r: A)(f: A => B): B = {
		try {
			f(r)
		} finally {
			r.close()
		}
	}
}
