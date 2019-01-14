package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import org.jsoup.nodes.Element
import java.io.File
import java.lang.IllegalArgumentException

class InterpretadorVB6Procedure(val elements: List<Element>, val arquivo: File) {
    private val matchDeclaracaoFuncao: MatchResult = let {
        //        var match: MatchResult? = null
        elements.forEach { element ->
            val match = rgxFuncaoCompleta.find(element.text().trim())
            if (match != null) return@let match
        }
        throw DeclaracaoDeFuncaoNaoDetectadaException("Declaração de função não identificada no 'VB6 Procedure' do arquivo '$arquivo'.")
    }
    private val listaParametros: List<ParametroDeFuncao> = let {
        val txtTodosOsParametros = matchDeclaracaoFuncao.groups["parametros"]!!.value
        val matchesParametros = rgxSepararParametros.findAll(txtTodosOsParametros)
        matchesParametros.map { match ->
            val isOptional = (match.groups["ehopcional"] != null)
            val passagem = Passagem.get(match.groups["valouref"]!!.value)
            val nome = match.groups["nomeparametro"]!!.value
            val tipoDeParametro = TipoDeParametro.get(match.groups["tipoparametro"]!!.value)
            val valorOpcional: String? = match.groups["valoropcional"]?.value
            val isArray = (match.groups["eharray"] != null)

            if (isOptional && valorOpcional == null) {
                throw IllegalArgumentException("O parâmetro '$nomeFuncao' está marcado como opcional, mas não possui valor padrão.")
            } else if (!isOptional && valorOpcional != null) {
                throw IllegalArgumentException("O parâmetro '$nomeFuncao' possui valor padrão, mas não está marcado como opcional.")
            }
            ParametroDeFuncao(
                nome = nome,
                isOptional = isOptional,
                isArray = isArray,
                passagem = passagem,
                tipoDeParametro = tipoDeParametro,
                valorOpcional = valorOpcional
            )
        }.toList()
    }

    val nomeFuncao: String = matchDeclaracaoFuncao.groups["nomefuncao"]!!.value
    val tipoDoRetorno: TipoDeParametro = let {
        val stringTipoRetorno = matchDeclaracaoFuncao.groups["tiporetorno"]!!.value
        try {
            return@let TipoDeParametro.get(stringTipoRetorno)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("O tipo de retorno '$stringTipoRetorno' não existe. Erro no arquivo '$arquivo'.")
        }
    }

    companion object {
        val rgxFuncaoCompleta =
            """Function\s*(?<nomefuncao>\w+)\s*\((?<parametros>.+)\)\s*As\s+(?<tiporetorno>\w+)""".toRegex()
        val rgxSepararParametros = """[^,]+""".toRegex()
        val rgxParametro =
            """(?<ehopcional>Optional)*\s*(?<valouref>\w+)\s+(?<nomeparametro>\w+)(?<eharray>\(\))*\s+As\s+(?<tipoparametro>\w+)(?:\s*=\s*(?<valoropcional>.*))?""".toRegex()
    }
}