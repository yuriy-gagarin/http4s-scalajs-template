package example

object Main extends Greeting {
  def main(args: Array[String]): Unit = {
    println(greeting)
  }
}

trait Greeting {
  lazy val greeting: String = "hello from js"
}
