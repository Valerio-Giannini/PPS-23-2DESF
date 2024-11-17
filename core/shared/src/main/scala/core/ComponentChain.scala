package core

import scala.annotation.showAsInfix

sealed trait ComponentChain extends Product with Iterable[Component]

object ComponentChain:

  def apply(): CNil.type = CNil

  def apply[C <: Component: ComponentTag](component: C): C :: CNil = component :: CNil

  implicit class CCOps[CC <: ComponentChain](chain: CC):
    def ::[C <: Component: ComponentTag](head: C): C :: CC = core.::(head, chain)


trait CNil extends ComponentChain:
  def ::[C <: Component: ComponentTag](head: C): C :: CNil = core.::(head, this)

case object CNil extends CNil:
  override def iterator = Iterator.empty

@showAsInfix
final case class ::[C <: Component: ComponentTag, S <: ComponentChain](h: C, t: S) extends ComponentChain:
  override def iterator = Iterator(h) ++ t.iterator

import scala.quoted.{Expr, Quotes, Type}

sealed trait ComponentChainTag[L <: ComponentChain]:
  def tags: Set[ComponentTag[_]]

inline given [L <: ComponentChain]: ComponentChainTag[L] = ${ deriveComponentChainTagImpl[L] }

private def deriveComponentChainTagImpl[L <: ComponentChain: Type](using
    quotes: Quotes
): Expr[ComponentChainTag[L]] =
  import quotes.reflect.*
  val typeReprOfL = TypeRepr.of[L]

  '{
    new ComponentChainTag[L]:
      override def tags: Set[ComponentTag[_]] = getTags[L].toSet
      override def toString: String           = tags.toString
      override def hashCode: Int              = tags.hashCode
      override def equals(obj: Any): Boolean = obj match
      case that: ComponentChainTag[?] => that.tags == this.tags
      case _                          => false
  }

inline private def getTags[L <: ComponentChain]: Seq[ComponentTag[_]] =
  import scala.compiletime.erasedValue
  inline erasedValue[L] match
  case _: (head :: tail) =>
    summon[ComponentTag[head]].asInstanceOf[ComponentTag[_]] +: getTags[tail]
  case _ => Seq()
