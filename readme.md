# KiteiArgs - ScalaFix rules

## What is this?

This is a set of [ScalaFix](https://scalacenter.github.io/scalafix/) rules to
help the rewriting of case classes that use default arguments so that they
no longer use default arguments. To ease pain of deprecation, we provide
a `of` method that can be used to create instances of the case class.

## Example

```scala
case class Foo(a: Int = 1, b: Int = 2)
```

will be rewritten to

```scala
case class Foo(a: Int, b: Int)
object Foo {
  def of(a: Int = 1, b: Int = 2): Foo = Foo(a, b)
}
```

## Caveats

This is going to generate ugly formatting. It is recommended to run
`scalafmt` after running this rule. 😛

## Your code is ugly

Yes, it is. This is my first attempt at writing a ScalaFix rule. But sure
enough, it works; or at least it works better than the previous attempt
of rewriting the code by hand using Vim macros.

## To develop rule:
```
sbt ~tests/test
# edit rules/src/main/scala/fix/Kiteiargs.scala
```

## License

The classic Mozilla one: This software is licensed under the MPL 2.0:

```
This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.
```