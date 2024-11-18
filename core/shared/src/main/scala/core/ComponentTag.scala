package core

import scala.quoted.{Expr, Quotes, Type}

/** Type class to keep information about the type of a subtype of Component */
trait ComponentTag[C]

inline given [C]: ComponentTag[C] = ${ deriveComponentTagImpl[C] }

private def deriveComponentTagImpl[C: Type](using quotes: Quotes): Expr[ComponentTag[C]] =
  import quotes.reflect.*

  val typeRepr = TypeRepr.of[C]
  if typeRepr =:= TypeRepr.of[Component] then
    report.error("Can only derive ComponentTags for subtypes of Component, not for Component itself.")
  else if !(typeRepr <:< TypeRepr.of[Component]) then report.error(s"${typeRepr.show} must be a subtype of Component")

  val computedString   = typeRepr.show
  val computedHashCode = computedString.hashCode

  '{
    new ComponentTag[C]:
      override def toString: String = ${ Expr(computedString) }
      override def hashCode: Int    = ${ Expr(computedHashCode) }
      override def equals(obj: Any): Boolean = obj match
      case that: ComponentTag[?] => (this eq that) || (this.hashCode == that.hashCode)
      case _                     => false
  }
