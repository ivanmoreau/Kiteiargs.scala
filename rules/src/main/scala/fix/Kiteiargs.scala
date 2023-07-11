/*
 * Copyright (c) 2023.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package fix

import scalafix.v1._
import scala.meta._

/** Kiteiargs: Remove default arguments from case class constructors,and replace
  * them with a function `#of` in the companion object that has the original
  * constructor semantics, having all arguments (both default and non-default)
  * as required. If the companion object does not exist, it is created.
  */
class Kiteiargs extends SemanticRule("Kiteiargs") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    var patches: Seq[Patch] = Seq.empty

    doc.tree.collect {
      case t: Defn.Class if t.mods.exists {
            case _: Mod.Case => {
              true
            }
            case mod => {
              false
            }
          } =>
        // List of all default and non-default arguments
        val oldArgs: Seq[Term.Param] = t.ctor.paramss.flatten

        // Remove all default arguments
        t.ctor.paramss.flatten.foreach { param: Term.Param =>
          param.default.foreach { _: Term =>
            patches = patches :+
              Patch.replaceTree(param, param.copy(default = None).syntax)
          }
        }

        // Create a new companion object if it does not exist
        doc.tree
          .collect {
            case o: Defn.Object if o.name.value == t.name.value =>
              Patch.empty
          }
          .headOption
          .getOrElse {
            patches = patches :+
              Patch.addRight(
                t,
                "\n" +
                  s"""object ${t.name.value} {
                 |  def of(${oldArgs
                    .map(_.copy(mods = List.empty))
                    .map(_.syntax)
                    .mkString(", ")}): ${t.name.value} = {
                 |    ${t.name.value}(${oldArgs
                    .map(_.name.value)
                    .mkString(", ")})
                 |  }
                 |}""".stripMargin.indent(t.tokens.head.pos.startColumn)
              )
          }

        // Create a new `of` function in the companion object
        doc.tree.collect {
          case o: Defn.Object if o.name.value == t.name.value =>
            // Create a new `of` function in the companion object
            val firstBrace = o.tokens.find(_.is[Token.LeftBrace]).get
            patches = patches :+
              Patch.addRight(
                firstBrace,
                s"""\n  def of(${oldArgs
                  .map(_.copy(mods = List.empty))
                  .map(_.syntax)
                  .mkString(", ")}): ${t.name.value} = {
                 |    ${t.name.value}(${oldArgs
                  .map(_.name.value)
                  .mkString(", ")})
                 |  }
                 |""".stripMargin.indent(o.tokens.head.pos.startColumn)
              )
        }

      case other => Patch.empty
    }

    patches.foldLeft(Patch.empty)(_ + _)
  }

}
