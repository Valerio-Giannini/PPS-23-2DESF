package mvc.model

/**
 * Condition encapsulates a predicate function, which can be evaluated to return a boolean value.
 */
trait Condition:
  var predicate: () => Boolean = () => true

  /**
   * Evaluates the predicate function and returns its boolean result.
   *
   * @return the result of the predicate function.
   */
  def evaluate: Boolean

  /**
   * Sets a new predicate function.
   *
   * @param condition a function that returns a boolean, representing the new predicate.
   */
  def setPredicate(condition: () => Boolean): Unit

object Condition:
  def apply(): Condition = new ConditionImpl
  
  private class ConditionImpl extends Condition:
  
    override def evaluate: Boolean = predicate()

    override def setPredicate(condition: () => Boolean): Unit =
      predicate = condition

