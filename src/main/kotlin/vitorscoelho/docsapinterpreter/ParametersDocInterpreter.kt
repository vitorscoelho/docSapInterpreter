package vitorscoelho.docsapinterpreter

import org.jsoup.Jsoup
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class SAPDoc(text: String) {
    val kotlinText: String

    init {
        val functionText: String = functionText(text)
        val docText: String = transformUnits(docText(text))
        this.kotlinText = "$docText\r\n$functionText"
//        println(this.kotlinText)
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
            .replace("[L2]", "(L2)")
            .replace("[L3]", "(L3)")
            .replace("[L4]", "(L4)")
            .replace("[L5]", "(L5)")
            .replace("[rad]", "(rad)")
            .replace("[F/L]", "(F/L)")
            .replace("[FL/L]", "(FL/L)")
            .replace("[FL]", "(FL)")
            .replace("[FL/rad]", "(FL/rad)")
            .replace("[s]", "(s)")
            .replace("[cyc/s]", "(cyc/s)")
            .replace("[L/s2]", "(L/s2)")
            .replace("[1/T]", "(1/T)")
            .replace("[ML2]", "(ML2)")
            .replace("[M]", "(M)")
            .replace("[s/L]", "(s/L)")
            .replace("[F/(L^cexp)]", "(F/(L^cexp))")
            .replace("[F/L3]", "(F/L3)")
            .replace("[M/L]", "(M/L)")
            .replace("[FL2]", "(FL2)")
            .replace("[Fs2]", "(Fs2)")
            .replace("[FLs2]", "(FLs2)")
            .replace("[1/s]", "(1/s)")
            .replace("[rad/s]", "(rad/s)")
            .replace("[rad2/s2]", "(rad2/s2)")
            .replace("[L/s]", "(L/s)")
            .replace("[rad/s]", "(rad/s)")
            .replace("[L2/L]", "(L2/L)")
    }

    private fun functionText(text: String): String {
        return functionInterpreter(
            text.substringBefore("<h2>Parameters</h2>").substringAfter("<h2>VB6 Procedure</h2>").substringAfter(
                "<p class=Comment>"
            ).substringBefore("</p>")
                .replace("ByVal", "ByVal ")
                .replace("ByRef", "ByRef ")
        )
    }

    private fun docText(text: String): String {
        //println(functionInterpreter(functionText))
        val docFunctionDescription =
            Jsoup.parse(text.substringAfter("<h2>Remarks</h2>").substringBefore("<h2>VBA Example</h2>"))
        val pDocFunctionDescription = docFunctionDescription.select("p")
        val haveReturnsText: Boolean
        val functionReturnsText = if (pDocFunctionDescription.last().text().contains("returns")) {
            haveReturnsText = true
            "@return${pDocFunctionDescription.last().text().substringAfter("returns")}"
        } else {
            haveReturnsText = false
            "@return AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        }
        val functionDescriptionText = StringBuilder().apply {
            append("/**\r\n* ")
            pDocFunctionDescription.subList(fromIndex = 0, toIndex = pDocFunctionDescription.lastIndex).forEach {
                append("${it.text()}\r\n* ")
            }
            if (!haveReturnsText) append("${pDocFunctionDescription[pDocFunctionDescription.lastIndex].text()}\r\n* ")
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