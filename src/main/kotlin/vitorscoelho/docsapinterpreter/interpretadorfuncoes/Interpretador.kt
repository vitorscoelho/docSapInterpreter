package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import vitorscoelho.docsapinterpreter.regexBreakLine
import vitorscoelho.docsapinterpreter.regexEmptySpaces
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import kotlin.math.max
import kotlin.math.min

class Interpretador(val arquivo: File) {
    private val texto =
        arquivo.readText()
            .replace("&quot;", "\"")
            .replace(regexBreakLine, " ")
            .replace(regexEmptySpaces, " ")

    val tituloArquivo: String
    val nomeFuncao: String
    val interpretadorSyntax: InterpretadorSyntax
    val interpretadorVB6Procedure: InterpretadorVB6Procedure
    val elementsParameters: List<Element>
    val elementsRemarks: List<Element>
    val elementsVBAExample: List<Element>
    val interpretadorReleaseNotes: InterpretadorReleaseNotes
    val elementsSeeAlso: List<Element>

    init {
        val textoJsoup: Document = Jsoup.parse(texto)

        val al = arrayListOf<Element>()
        val topicos =
            listOf("Syntax", "VB6 Procedure", "Parameters", "Remarks", "VBA Example", "Release Notes", "See Also")
        var indiceTopicos = 0
        val listasElements = arrayListOf<List<Element>>()
        var funcao = ""
        val todosElementos = textoJsoup.body().allElements
        todosElementos.forEachIndexed { index, element ->
            if (element.tag().name == "h1") {
                funcao = element.text()
            } else if (element.tag().name == "h2" && element.text() == topicos[indiceTopicos]) {
                listasElements.add(al.toList())
                indiceTopicos = min(indiceTopicos + 1, topicos.lastIndex)
                al.clear()
            } else {
                val elementAnterior = todosElementos[max(0, index - 1)]
                if (element.parent() != elementAnterior) {
                    al.add(element)
                }
            }
        }
        listasElements.add(al.toList())
        tituloArquivo = textoJsoup.title()
        nomeFuncao = funcao
        try {
            interpretadorSyntax = InterpretadorSyntax(listasElements[1])
            interpretadorVB6Procedure = InterpretadorVB6Procedure(listasElements[2])
            elementsParameters = listasElements[3]
            elementsRemarks = listasElements[4]
            elementsVBAExample = listasElements[5]
            interpretadorReleaseNotes = InterpretadorReleaseNotes(elements = listasElements[6])
            elementsSeeAlso = listasElements[7]
        } catch (e: IndexOutOfBoundsException) {
            throw NaoEhUmArquivoDocumentacaoSap2000(
                "Não é possível interpretar o arquivo $arquivo como um arquivo de documentação do SAP2000.\r\n" +
                        "Não possui um dos campos: 'Syntax', 'VB6 Procedure', 'Parameters', 'Remarks', 'VBA Example', 'Release Notes', 'See Also'."
            )
        }
    }
}