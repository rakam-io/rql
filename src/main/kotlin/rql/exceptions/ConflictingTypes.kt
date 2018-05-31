package rql.exceptions

import rql.RqlType

/**
 * Thrown a variable has confliciting types
 * @param variable The variable.
 * @param types The types of variable.
 */
class ConflictingTypes(variable: String, types: Set<RqlType>)
    : RqlStaticException() {

    override val message: String =
            "Variable '${variable}' has conflicting types. [ ${toString(types)} ]"

    private fun toString(keys: Set<RqlType>): String {
        return keys.map { key -> "'${key.toString()}'" }.joinToString()
    }
}
