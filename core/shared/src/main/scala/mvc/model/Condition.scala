package mvc.model


trait Condition:
  def evaluate: Boolean
  def setPredicate(condition: () => Boolean): Unit
/**
 * A `Condition` class that encapsulates a predicate function, which can be evaluated
 * to return a boolean value.
 *
 * The predicate is initially set to always return `true`, but it can be changed 
 * using the `setPredicate` method.
 */
object Condition:
  def apply(): Condition = new ConditionImpl
  
  private class ConditionImpl extends Condition:
  
    private var predicate: () => Boolean = () => true
  
    /**
     * Evaluates the predicate function and returns its boolean result.
     *
     * @return the result of the predicate function.
     */
    def evaluate: Boolean = predicate()
  
    /**
     * Sets a new predicate function.
     *
     * @param condition a function that returns a boolean, representing the new predicate.
     */
    def setPredicate(condition: () => Boolean): Unit =
      predicate = condition

