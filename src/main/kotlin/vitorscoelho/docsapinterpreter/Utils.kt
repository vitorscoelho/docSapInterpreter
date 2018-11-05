package vitorscoelho.docsapinterpreter

internal fun toJavaConvention(name: String): String {
    if (name.all { it.isUpperCase() }) return name
    val firstChar = name[0].toLowerCase()
    return firstChar + name.substring(startIndex = 1).replaceFirst("()", "")
}

internal val regexEmptySpaces = "\\s+".toRegex()
internal val regexBreakLine = "\r\n".toRegex()
internal const val PARAMETER_DELIMITER = "¬"

internal fun Int.isEven(): Boolean = this % 2 == 0
internal fun Int.isOdd(): Boolean = !this.isEven()