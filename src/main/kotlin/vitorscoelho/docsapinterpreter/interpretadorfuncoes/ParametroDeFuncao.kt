package vitorscoelho.docsapinterpreter.interpretadorfuncoes

class ParametroDeFuncao(
    val isOptional: Boolean,
    val passagem: Passagem,
    val nome: String,
    val tipoDeParametro: TipoDeParametro,
    val valorOpcional: String?,
    val isArray: Boolean
)