package vitorscoelho.docsapinterpreter

import org.jsoup.Jsoup
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class SAPDoc(text: String) {
    val kotlinText: String

    init {
        val textReplaceUnits = transformUnits(text)
        val functionText: String = functionText(textReplaceUnits)
        val docText: String = docText(textReplaceUnits)
        this.kotlinText = "$docText\r\n$functionText"
        println(this.kotlinText)
    }

    private fun transformUnits(text: String): String {
        return text
            .replace("[F]", "(F)")
            .replace("[F/L2]", "(F/L2)")
            .replace("[1/L]", "(1/L)")
            .replace("[L/L]", "(L/L)")
            .replace("[T]", "(T)")
            .replace("[T/L]", "(T/L)")
            .replace("[deg]", "(deg)")
            .replace("[L]", "(L)")
            .replace("[rad]", "(rad)")
            .replace("[F/L]", "(F/L)")
            .replace("[FL/L]", "(FL/L)")
            .replace("[FL]", "(FL)")
            .replace("[FL/rad]", "(FL/rad)")
            .replace("[s]", "(s)")
            .replace("[cyc/s]", "(cyc/s)")
            .replace("[L/s2]","(L/s2)")
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
            throw IllegalArgumentException("This text has no 'returns' field. (${functionText(text)})")
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