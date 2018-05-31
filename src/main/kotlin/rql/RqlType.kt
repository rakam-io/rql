package rql

enum class RqlType {
    PRIMITIVE, // java.lang.String, java.lang.Long, java.time.LocalDate
    OBJECT,
    ARRAY
}

typealias Anything = Any?

typealias Variables = MutableMap<String, MutableSet<RqlType>>
