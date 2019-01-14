package vitorscoelho.docsapinterpreter.interpretadorfuncoes

import java.lang.Exception

class NaoEhUmArquivoDocumentacaoSap2000(message:String):Exception(message)

class VersaoNaoDetectadaException(message:String):Exception(message)

class DeclaracaoDeFuncaoNaoDetectadaException(message: String):Exception(message)