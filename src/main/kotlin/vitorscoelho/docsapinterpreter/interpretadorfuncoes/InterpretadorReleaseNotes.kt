package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.nodes.Element
import java.io.File

class InterpretadorReleaseNotes(val elements: List<Element>, val arquivo: File) {
    val versaoInicial: String = let {
        var valor = ""
        elements.forEach { element ->
            val elementTextUpperCase = element.text().toUpperCase()
            if (elementTextUpperCase.contains(textoAProcurarPelaVersao, ignoreCase = true)) {
                valor = regexVersao.find(elementTextUpperCase)?.value ?: ""
            }
        }
        if (valor == "") throw VersaoNaoDetectadaException("Versão não identificada no arquivo '$arquivo'.")
        valor
    }

    val versaoInicialSemSubversao = regexVersaoSemSubversoes.find(versaoInicial)!!.value.toInt()

    companion object {
        private val textoAProcurarPelaVersao = "Initial release".toUpperCase()
        private val regexVersao = """[\d.]+\d""".toRegex()
        private val regexVersaoSemSubversoes = """\d+""".toRegex()
    }
}