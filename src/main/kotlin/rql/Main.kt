@file:JvmName("Main")

package rql

/**
 * Infer the types of the variables in the template string.
 * @param args The commandline arguments. The first argument is the template string.
 */
fun main(args: Array<String>) {
	if (args.isNotEmpty()) {

		var settings = Settings()
		settings.checkType = false
		val rql = Rql.parse(args[0], settings)

		for ( (variable, types) in rql.variables){
			println("Variable '${variable}' has type [ ${toString(types)} ]")
		}
	}
}

private fun toString(keys: Set<RqlType>): String {
	return keys.map { key -> "'${key.toString()}'" }.joinToString()
}
