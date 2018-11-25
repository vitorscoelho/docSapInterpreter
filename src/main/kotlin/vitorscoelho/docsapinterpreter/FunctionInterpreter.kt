package vitorscoelho.docsapinterpreter

import java.lang.StringBuilder

/*
Function SetLoadDistributed(ByVal Name As String, ByVal
 LoadPat As String, ByVal MyType As Long, ByVal Dir As Long, ByVal Dist1
 As Double, ByVal Dist2 As Double, ByVal Val1 As Double, ByVal Val2 As
 Double, Optional ByVal CSys As String = &quot;Global&quot;, Optional ByVal
 RelDist As Boolean = True, Optional ByVal Replace As Boolean = True, Optional
 ByVal ItemType As eItemType = Object) As Long
 */

internal fun functionInterpreter(text: String): String {
    return FunctionSAPDoc(text).kotlinText
}

private class FunctionSAPDoc(text: String) {
    private val newText = text.replace("&quot;", "\"").replace(regexBreakLine, " ").replace(regexEmptySpaces, " ")
    val functionNameSAP = newText.substringBefore('(').split(regexEmptySpaces)[1]
    val functionName: String = toJavaConvention(functionNameSAP)
    val returnType = ParameterType.get(newText.split(regexEmptySpaces).last())
    val parameters: List<FunctionParameter>
    val kotlinText: String

    init {
        //fun addByPoint(point1: String, point2: String, name: StringByRef = StringByRef.NONE, propName: String = "Default", userName: String = ""): Int =
        val parametersText = let {
            val textWithoutParenthesis = newText.substringAfter('(').substringBeforeLast(')')
            val commasIndexes = arrayListOf<Int>()
            textWithoutParenthesis.forEachIndexed { index, char ->
                if (char == ',') {
                    //Testing if inside a string
                    val textBefore = textWithoutParenthesis.substring(startIndex = 0, endIndex = index)
                    val textAfter = textWithoutParenthesis.substring(startIndex = index + 1)
                    val countQuotBefore = textBefore.count { charBefore -> charBefore == '"' }
                    val countQuotAfter = textAfter.count { charAfter -> charAfter == '"' }
                    if ((countQuotAfter.isEven() || countQuotAfter == 0) && (countQuotBefore.isEven() || countQuotBefore == 0)) commasIndexes.add(
                        index
                    )
                }
            }
            StringBuilder(textWithoutParenthesis).apply {
                commasIndexes.forEach { index -> replace(index, index + 1, PARAMETER_DELIMITER) }
            }.toString()
        }
        this.parameters = if (parametersText.trim().isEmpty()) {
            emptyList()
        } else {
            parametersText.split(PARAMETER_DELIMITER).map { FunctionParameter(text = it.trim()) }
        }

        val callNativeFunction = StringBuilder().apply {
            append("=\r\ncallFunction${returnType.kotlinText}(\"")
            append(functionNameSAP)
            append("\"")
            parameters.forEach { parameter ->
                append(",${parameter.name}")
            }
            append(")")
        }.toString()

        this.kotlinText = StringBuilder().apply {
            append("fun ")
            append("$functionName (")
            parameters.forEach { parameter -> append("${parameter.kotlinText},") }
            deleteCharAt(lastIndex)
            append("):${returnType.kotlinText}")
            append(callNativeFunction)
        }.toString()
    }
}

private class FunctionParameter(val text: String) {
    val isOptional: Boolean
    val isByRef: Boolean
    val name: String
    val parameterType: ParameterType
    val optionalValue: String?
    val isArray: Boolean
    val kotlinText: String

    init {
//        println(text)
        val list = text.replace("Optional ByVal", "OptionalByVal").replace("Optional ByRef", "OptionalByRef")
            .split(regexEmptySpaces)
//        println(list)
        this.isOptional = list[prefixIndex].contains("Optional")
        this.isByRef = list[prefixIndex].contains("ByRef")
        this.name = toJavaConvention(name = list[nameIndex])
        this.parameterType = ParameterType.get(text = list[parameterTypeIndex])
        this.isArray = list[1].contains("()")
        this.optionalValue = if (text.contains('=')) {
            text.substring(startIndex = text.indexOf('=') + 1).trim()
        } else {
            null
        }
        this.kotlinText = StringBuilder().apply {
            append(name)
            append(":")
            append(parameterType.kotlinText)
            if (isArray) append("Array")
            if (isByRef) append("ByRef")
            if (isOptional) {
                append("=")
                if (optionalValue != null) {
                    if (parameterType == ParameterType.BOOLEAN) {
                        append(optionalValue.replace('T', 't').replace('F', 'f'))
                    } else {
                        if (parameterType.isEnum) {
                            append("${parameterType.enumClassName}.$optionalValue.sapId")
                        } else {
                            append("$optionalValue")
                        }
                    }
                }
            }
        }.toString()
    }

    companion object {
        private const val prefixIndex = 0
        private const val nameIndex = 1
        private const val parameterTypeIndex = 3
    }
}

private enum class ParameterType(
    val sapText: String,
    val kotlinText: String,
    val isEnum: Boolean,
    val enumClassName: String
) {
    STRING(sapText = "String", kotlinText = "String", isEnum = false, enumClassName = ""),
    BOOLEAN(sapText = "Boolean", kotlinText = "Boolean", isEnum = false, enumClassName = ""),
    DOUBLE(sapText = "Double", kotlinText = "Double", isEnum = false, enumClassName = ""),
    INT(sapText = "Long", kotlinText = "Int", isEnum = false, enumClassName = ""),
    UNITS(sapText = "eUnits", kotlinText = "Int", isEnum = true, enumClassName = "Units"),
    MAT_TYPE(sapText = "eMatType", kotlinText = "Int", isEnum = true, enumClassName = "MatType"),
    ITEM_TYPE(sapText = "eItemType", kotlinText = "Int", isEnum = true, enumClassName = "ItemType"),
    LOAD_PATTERN_TYPE(
        sapText = "eLoadPatternType",
        kotlinText = "Int",
        isEnum = true,
        enumClassName = "LoadPatternType"
    ),
    C_TYPE(sapText = "eCType", kotlinText = "Int", isEnum = true, enumClassName = "CType"),
    ITEM_TYPE_ELM(sapText = "eItemTypeElm", kotlinText = "Int", isEnum = true, enumClassName = "ItemTypeElm"),
    E2D_FRAME_TYPE(sapText = "e2DFrameType", kotlinText = "Int", isEnum = true, enumClassName = "E2DFrameType"),
    E3D_FRAME_TYPE(sapText = "e3DFrameType", kotlinText = "Int", isEnum = true, enumClassName = "E3DFrameType"),
    CONSTRAINT_TYPE(sapText = "eConstraintType", kotlinText = "Int", isEnum = true, enumClassName = "ConstraintType"),
    CONSTRAINT_AXIS(sapText = "eConstraintAxis", kotlinText = "Int", isEnum = true, enumClassName = "ConstraintAxis"),
    LOAD_CASE_TYPE(sapText = "eLoadCaseType", kotlinText = "Int", isEnum = true, enumClassName = "LoadCaseType");

    companion object {
        private val map = ParameterType.values().associate { it.sapText to it }

        fun get(text: String) =
            map.getOrElse(key = text) { throw IllegalArgumentException("ParameterType '$text' nonexistent.") }
    }
}