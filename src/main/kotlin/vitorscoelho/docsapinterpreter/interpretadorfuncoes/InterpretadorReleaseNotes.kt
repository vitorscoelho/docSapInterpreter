package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.nodes.Element

class InterpretadorReleaseNotes(val elements: List<Element>) {
    val versaoInicial: String = let {
        var valor = ""
        elements.forEach { element ->
            if (element.text().contains(textoAProcurarPelaVersao)) {
                valor = element.text().substringAfter(textoAProcurarPelaVersao)
                if (valor.endsWith('.')) valor = valor.dropLast(1)
            }
        }
        valor
    }
//    val versaoInicialPrograma:

    companion object {
        private val textoAProcurarPelaVersao = "Initial release in version "
    }
}