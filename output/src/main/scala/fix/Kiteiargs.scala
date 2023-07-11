package fix

object Kiteiargs {
  // Without companion object
  case class Foo(a: Int, b: Int, c: Int)
  object Foo {
    def of(a: Int = 1, b: Int = 2, c: Int = 3): Foo = {
      Foo(a, b, c)
    }
  }

  // With companion object
  case class Bar(a: Int, b: Int, c: Int)
  object Bar {
    def of(a: Int = 1, b: Int = 2, c: Int = 3): Bar = {
      Bar(a, b, c)
    }
  }
}
