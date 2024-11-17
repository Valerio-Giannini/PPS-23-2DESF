package dsl

import dsl.coreDSL.CoreDSL

/**
 * Entry point of the DSL keywords.
 * Importing `DSL.*` will make all the keywords of the language available.
 */
object DSL:
  export CoreDSL.*

