package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import vitorscoelho.docsapinterpreter.regexEmptySpaces

class ParametroDeFuncao(val text: String) {
    val isOptional: Boolean
    val isByRef: Boolean
    val nome: String
    val tipoDeParametro: TipoDeParametro
    val valorOpcional: String?
    val isArray: Boolean

    init {
//        println(text)
        val list = text
            .replace("Optional ByVal", "OptionalByVal")
            .replace("Optional ByRef", "OptionalByRef")
            .split(regexEmptySpaces)
//        println(list)
        this.nome = list[nameIndex]
        this.isOptional = list[prefixIndex].contains("Optional")
        this.isByRef = list[prefixIndex].contains("ByRef")
        this.tipoDeParametro = TipoDeParametro.get(text = list[parameterTypeIndex])
        this.isArray = list[1].contains("()")
        this.valorOpcional = if (text.contains('=')) {
            text.substring(startIndex = text.indexOf('=') + 1).trim()
        } else {
            null
        }
    }

    companion object {
        private const val prefixIndex = 0
        private const val nameIndex = 1
        private const val parameterTypeIndex = 3
    }
}