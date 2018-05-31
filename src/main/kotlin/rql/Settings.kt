package rql

/**
 * Represents the settings for the parsing and rendering of the template string.
 */
class Settings {
    /**
     * True to throw an exception when a non-valid type is rendered.
     */
    var onlyValidTypes = true;
    /**
     * The set of valid types.
     */
    var validTypes: Set<String> = setOf(
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.String",
            "java.time.LocalDate"
    )
    /**
     * True to strip newlines from the template string.
     */
		var stripNewlines = false
    /**
     * True to check the type of variables.
     */
    var checkType = true
}
