package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import vitorscoelho.docsapinterpreter.regexBreakLine
import vitorscoelho.docsapinterpreter.regexEmptySpaces
import java.io.File

private val topicos =
    listOf("Syntax", "VB6 Procedure", /*"Parameters",*/ "Remarks", /*"VBA Example",*/ "Release Notes" /*,"See Also"*/)

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
    val elementsParameters: List<Element>?
    val elementsRemarks: List<Element>
    val elementsVBAExample: List<Element>?
    val interpretadorReleaseNotes: InterpretadorReleaseNotes
    val elementsSeeAlso: List<Element>?

    init {
        val textoJsoup: Document = Jsoup.parse(texto)

        val todosElementos = textoJsoup.body().allElements

        val alCampo = arrayListOf<String>()
        val alIndiceInicial = arrayListOf<Int>()
        val alIndiceFinal = arrayListOf<Int>()

        todosElementos.forEachIndexed { index, element ->
            if (element.tag().name == "h2" && topicos.contains(element.text())) {
                alCampo.add(element.text())
                alIndiceInicial.add(index)
                if (alIndiceInicial.size != 1) alIndiceFinal.add(index)
            }
        }
        alIndiceFinal.add(todosElementos.lastIndex + 1)

        topicos.forEach { campo ->
            if (!alCampo.contains(campo)) {
                throw NaoEhUmArquivoDocumentacaoSap2000(
                    "Não é possível interpretar o arquivo '$arquivo' como um arquivo de documentação do SAP2000. Não possui o campo '$campo'."
                )
            }
        }
        val mapCampoElements = hashMapOf<String, List<Element>>()
        alCampo.forEachIndexed { index, campo ->
            mapCampoElements[campo] =
                    todosElementos.subList(fromIndex = alIndiceInicial[index], toIndex = alIndiceFinal[index])
        }

        tituloArquivo = textoJsoup.title()
        nomeFuncao = textoJsoup.select("h1").text()
        interpretadorSyntax = InterpretadorSyntax(mapCampoElements["Syntax"]!!)
        interpretadorVB6Procedure = InterpretadorVB6Procedure(mapCampoElements["VB6 Procedure"]!!)
        elementsParameters = mapCampoElements["Parameters"]
        elementsRemarks = mapCampoElements["Remarks"]!!
        elementsVBAExample = mapCampoElements["VBA Example"]
        interpretadorReleaseNotes = InterpretadorReleaseNotes(elements = mapCampoElements["Release Notes"]!!,arquivo = arquivo)
        elementsSeeAlso = mapCampoElements["See Also"]
    }
}