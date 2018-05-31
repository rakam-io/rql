package rql.exceptions

import rql.Anything

/**
 * Thrown when a variable doesn't have a member
 * @param variable The variable.
 * @param missing The missing member.
 * @param members The members of the variable
 */
class PropertyNotFound(variable: String, missing: String, members: Set<Anything>)
    : RqlRunTimeException() {
    override val message: String =
            "Variable '${variable}' doesn't have a property '${missing}'. It has the following properties: ${toString(members)}"

    private fun toString(keys: Set<Anything>): String {
        return keys.map { key -> "'${key.toString()}'" }.joinToString()
    }
}
