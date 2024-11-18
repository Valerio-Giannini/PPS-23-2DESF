package core

import scala.annotation.showAsInfix

/** An heterogeneous data structure to memorize only specific subtype of [[Component]].
  */
sealed trait ComponentChain extends Product with Iterable[Component]

object ComponentChain:

  def apply(): CNil.type = CNil

  def apply[C <: Component: ComponentTag](component: C): C ::: CNil = component ::: CNil

  implicit class CCOps[CC <: ComponentChain](chain: CC):
    def :::[C <: Component: ComponentTag](head: C): C ::: CC = core.:::(head, chain)

/** Empty instance of [[ComponentChain]]
  */
trait CNil extends ComponentChain:
  def :::[C <: Component: ComponentTag](head: C): C ::: CNil = core.:::(head, this)

case object CNil extends CNil:
  override def iterator: Iterator[Nothing] = Iterator.empty

/** Constructor of [[ComponentChain]]
  * @param h
  *   head of the structure
  * @param t
  *   tail of the structure
  * @tparam C
  *   type of the head
  * @tparam S
  *   type of the tail
  */
@showAsInfix
final case class :::[C <: Component: ComponentTag, S <: ComponentChain](h: C, t: S) extends ComponentChain:
  override def iterator = Iterator(h) ++ t.iterator

import scala.quoted.{Expr, Quotes, Type}

/** Type class to keep information about the type of a [[ComponentChain]].
  * @tparam L
  */
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

/** Extracts the sequence of component tags for a given [[ComponentChainTag]].
  *
  * @tparam L
  *   The type of the component chain.
  * @return
  *   A sequence of [[ComponentTag]].
  */
inline private def getTags[L <: ComponentChain]: Seq[ComponentTag[_]] =
  import scala.compiletime.erasedValue
  inline erasedValue[L] match
  case _: (head ::: tail) =>
    summon[ComponentTag[head]].asInstanceOf[ComponentTag[_]] +: getTags[tail]
  case _ => Seq()
