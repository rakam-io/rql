package rql

/**
 * Represents variables in scope.
 */
class Bindings {
    private val stack = mutableListOf<Map<String, Anything>>()
    /**
     * Destroy the current scope.
     */
    fun pop(): Map<String, Anything> {
        return stack.removeAt(0)
    }
    /**
     * Replace the current scope with a new scope.
     */
    fun push(variables: Map<String, Anything> = mutableMapOf<String, Anything>()) {
        stack.add(0, variables)
    }
    operator fun get(variable: String): Anything {
        return stack.first()[variable]
    }
    fun contains(variable: String): Boolean {
        return stack.first().contains(variable)
    }
}

