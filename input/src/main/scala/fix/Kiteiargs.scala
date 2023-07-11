/*
rule = Kiteiargs
 */
package fix

object Kiteiargs {
  // Without companion object
  case class Foo(a: Int = 1, b: Int = 2, c: Int = 3)

  // With companion object
  case class Bar(a: Int = 1, b: Int = 2, c: Int = 3)
  object Bar {}

  // With companion object and non-default arguments
  case class Baz(a: Int = 1, b: Int, c: Int = 3)
  object Baz {}

  // Without companion object and without default arguments
  case class Qux(a: Int, b: Int, c: Int)

  // With companion object and without default arguments
  case class Quux(a: Int, b: Int, c: Int)
  object Quux {}
}
