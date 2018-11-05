package vitorscoelho.docsapinterpreter

import org.jsoup.Jsoup
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class SAPDoc(text: String) {
    val kotlinText: String

    init {
        val functionText: String = functionText(text)
        val docText: String = docText(text)
        this.kotlinText = "$docText\r\n$functionText"
        println(this.kotlinText)
    }

    private fun functionText(text: String): String {
        return functionInterpreter(
            text.substringBefore("<h2>Parameters</h2>").substringAfter("<h2>VB6 Procedure</h2>").substringAfter(
                "<p class=Comment>"
            ).substringBefore("</p>")
        )
    }

    private fun docText(text: String): String {
        //println(functionInterpreter(functionText))
        val docFunctionDescription =
            Jsoup.parse(text.substringAfter("<h2>Remarks</h2>").substringBefore("<h2>VBA Example</h2>"))
        val pDocFunctionDescription = docFunctionDescription.select("p")
        val functionDescriptionText = StringBuilder().apply {
            append("/**\r\n* ")
            pDocFunctionDescription.subList(fromIndex = 0, toIndex = pDocFunctionDescription.lastIndex).forEach {
                append("${it.text()}\r\n* ")
            }
        }
        val functionReturnsText = if (pDocFunctionDescription.last().text().contains("returns")) {
            "@return${pDocFunctionDescription.last().text().substringAfter("returns")}"
        } else {
            throw IllegalArgumentException("This text has no 'returns' field.")
        }

        val doc = Jsoup.parse(text.substringAfter("<h2>Parameters</h2>").substringBefore("<h2>Remarks</h2>"))
        val stringBuilder = StringBuilder(functionDescriptionText)
        doc.select("p").forEach { p ->
            val pClass = p.className()
            val content = p.text()
            if (content == "") {
            } else if (pClass == "ParameterName") {
                stringBuilder.append("@param ${toJavaConvention(name = content)} ")
            } else if (pClass == "Comment") {
                stringBuilder.append("$content\r\n* ")
            } else if (pClass == "ParameterDescription") {
                stringBuilder.append("* $content\r\n* ")
            }
        }
        stringBuilder.append(functionReturnsText)
        stringBuilder.append("\r\n*/")
        return stringBuilder.toString()
    }
}
/**
 * This function adds a new frame object whose end points are specified by name.
 * @param point1 The name of a defined point object at the I-End of the added frame object.
 * @param point2 The name of a defined point object at the J-End of the added frame object.
 * @param name This is the name that the program ultimately assigns for the frame object. If no UserName is specified, the program assigns a default name to the frame object. If a UserName is specified and that name is not used for another frame, cable or tendon object, the UserName is assigned to the frame object, otherwise a default name is assigned to the frame object.
 * @param propName This is Default, None, or the name of a defined frame section property. If it is Default, the program assigns a default section property to the frame object. If it is None, no section property is assigned to the frame object. If it is the name of a defined frame section property, that property is assigned to the frame object.
 * @param userName This is an optional user specified name for the frame object. If a UserName is specified and that name is already used for another frame object, the program ignores the UserName.
 * @return zero if the frame object is successfully added, otherwise it returns a nonzero value.
 */
//fun addByPoint(point1: String, point2: String, name: StringByRef = StringByRef.NONE, propName: String = "Default", userName: String = ""): Int =
//      callFunction("AddByPoint", point1, point2, name.variant, propName, userName).int