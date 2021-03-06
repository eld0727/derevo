package org.manatki.derevo

package catsInstances
import _root_.cats.Show
import magnolia.{CaseClass, Magnolia, SealedTrait}

object show extends Derivation[Show] {
  type Typeclass[T] = Show[T]

  def combine[T](ctx: CaseClass[Show, T]): Show[T] = new Show[T] {
    def show(value: T): String = ctx.parameters.map { p =>
      s"${p.label}=${p.typeclass.show(p.dereference(value))}"
    }.mkString(s"${ctx.typeName.short}{", ",", "}")
  }

  def dispatch[T](ctx: SealedTrait[Show, T]): Show[T] =
    new Show[T] {
      def show(value: T): String = ctx.dispatch(value) { sub =>
        sub.typeclass.show(sub.cast(value))
      }
    }

  implicit def instance[T]: Show[T] = macro Magnolia.gen[T]
}
