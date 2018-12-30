package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.nodes.Element

class InterpretadorSyntax(val elements: List<Element>) {
    val nomeFuncao: String = elements[0].text().substringAfterLast('.').trim()
}