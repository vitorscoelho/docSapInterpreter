package vitorscoelho.docsapinterpreter.interpretadorfuncoes

enum class Passagem{
    BY_REF,
    BY_VAL;

    companion object {
        fun get(text: String):Passagem {
            if(text.toUpperCase()=="BYVAL"){
                return BY_VAL
            }
            if(text.toUpperCase()=="BYREF"){
                return BY_REF
            }
            throw IllegalArgumentException("Passagem '$text' inexistente.")
        }
    }
}