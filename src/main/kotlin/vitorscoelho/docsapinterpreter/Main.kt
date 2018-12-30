package vitorscoelho.docsapinterpreter

import vitorscoelho.docsapinterpreter.filecomparator.Pasta
import vitorscoelho.docsapinterpreter.interpretadorfuncoes.Interpretador
import vitorscoelho.docsapinterpreter.interpretadorfuncoes.NaoEhUmArquivoDocumentacaoSap2000
import java.io.File

fun main() {
    val caminhoPasta = "/home/vitor/Pasta compartilhada VM/Sap20 Documentation/SAP2000_API_Fuctions"
    val caminhoArquivo =
        "/home/vitor/Pasta compartilhada VM/Sap20 Documentation/SAP2000_API_Fuctions/Object_Model/Frame_Object/SetLoadTransfer.htm"
    val interpretador = Interpretador(File(caminhoArquivo))
    println(interpretador.tituloArquivo)
    println(interpretador.interpretadorReleaseNotes.versaoInicial)
    val arquivosNaoSap = arrayListOf<File>()
    val versoes= hashSetOf<String>()
    Pasta.getArquivos(pasta = File(caminhoPasta)).forEach {
        try {
            val interpretador2 = Interpretador(it)
            versoes.add(interpretador2.interpretadorReleaseNotes.versaoInicial)
//            println(interpretador2.interpretadorReleaseNotes.versaoInicial)
        } catch (e: NaoEhUmArquivoDocumentacaoSap2000) {
            arquivosNaoSap.add(it)
        }
    }
    versoes.sorted().forEach { println(it) }

//    interpretador.elementsSyntax.forEach { println(it.text()) }
//    interpretador.elementsVB6Procedure.forEach { println(it.text()) }
//    interpretador.elementsParameters.forEach { println(it.text()) }
//    interpretador.elementsRemarks.forEach { println(it.text()) }
//    interpretador.elementsVBAExample.forEach { println(it.text()) }
//    interpretador.elementsReleaseNotes.forEach { println(it.text()) }
//    interpretador.elementsSeeAlso.forEach { println(it.text()) }

//        if (interpretador.textoSeeAlso.isNotBlank() && interpretador.textoSeeAlso.contains("meta name="))
//        println(interpretador.textoSeeAlso)
    //}

//    val caminhoPasta="C:\\Users\\vitor\\IdeaProjects\\sw4k\\src\\main\\kotlin\\vitorscoelho\\sw4k\\sapversions\\v14\\sapmodel\\design\\coldformed"
//    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
//        println("val ${file.nameWithoutExtension}:${file.nameWithoutExtension}V14")
//    }
//    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
//        println("override val ${file.nameWithoutExtension}=${file.nameWithoutExtension}(programName)")
//    }
//    val caminhoArquivo =
//        "/home/vitor/Pasta compartilhada VM/Sap15 Documentation/SAP2000_API_Fuctions/Obsolete_Functions/SetPreference_{Steel_Chinese_2002}.htm"
//    val sapDoc = SAPDoc(File(caminhoArquivo).readText())
//    println(sapDoc.kotlinText)
//
//    val expressionReplaceWith = StringBuilder().apply {
//        append(sapDoc.functionSAPDoc.functionName)
//        append("(")
//        sapDoc.functionSAPDoc.parameters.forEachIndexed { index, functionParameter ->
//            append(functionParameter.name)
//            if (index == sapDoc.functionSAPDoc.parameters.lastIndex) {
//                append(")")
//            } else {
//                append(",")
//            }
//        }
//    }.toString()
//
//    val deprecatedExpression = StringBuilder().apply {
//        append("@Deprecated(\r\n")
//        append("message = ,\r\n")
//        append("replaceWith = ReplaceWith(\r\n")
//        append("expression = \"$expressionReplaceWith\",\r\n")
//        append("imports = arrayOf()\r\n")
//        append("),\r\n")
//        append("level = DeprecationLevel.WARNING\r\n")
//        append(")")
//    }.toString()
//    println(deprecatedExpression)
//
//    val pasta1=Pasta(file = File("/home/vitor/Pasta compartilhada VM/Sap14 Documentation/SAP2000_API_Fuctions"))
//    val pasta2=Pasta(file=File("/home/vitor/Pasta compartilhada VM/Sap15 Documentation/SAP2000_API_Fuctions"))
//
//    val comparador=ComparadorDePastas(pasta1=pasta1,pasta2 = pasta2)
//    println(comparador.arquivosComMesmoNomeMasComConteudoDiferente[0])


//    val caminhoPasta =
//        "/home/vitor/Pasta compartilhada VM/Teste/SAP2000_API_Fuctions/Definitions/Load_Pattern/Auto_Wind_Load"
//    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
//        if (file.isFile) {
////            println("Nome do arquivo: ${file.name}")
//            val sapdoc = SAPDoc(file.readText())
//            println(sapdoc.kotlinText)
//        }
//    }
//    println("Total de funções = ${File(caminhoPasta).listFiles().filter { it.isFile }.size}")

//    val pastaDesign =
//        File("C:\\Users\\vitor\\Downloads\\API_Sap2000v14\\SAP2000 v14 documentation\\SAP2000_API_Fuctions\\Design\\Steel\\")
//    val enderecoParaSalvarArquivoKotlin =
//        "C:\\Users\\vitor\\Downloads\\API_Sap2000v14\\SAP2000 v14 documentation\\Classes Kotlin criadas\\"
//
//
//
//    pastaDesign.listFiles().filter { it.isDirectory }.forEach { subpasta ->
//        val nomeClasse = subpasta.name
//        val nomeObjectoCOM = nomeClasse
//        val inicioDoTexto = "import vitorscoelho.sw4k.comutils.*\r\n" +
//                "import vitorscoelho.sw4k.sap14.enums.ItemType\r\n" +
//                "class $nomeClasse internal constructor(programName: String) : ${nomeClasse}V14 {\r\n" +
//                "    override val activeXComponentName: String = \"\$programName.cDSt$nomeObjectoCOM\"\r\n" +
//                "}\n" +
//                "\n" +
//                "interface ${nomeClasse}V14 : SapComponent {"
//
//        val fimDoTexto = "}"
//        println("Pasta: ${subpasta.name}")
//        val kotlinFileCreator = FileCreator(
//            fileInit = inicioDoTexto,
//            fileEnd = fimDoTexto,
//            docFolder = subpasta
//        )
//
//        val textoCorrigidoParaDesignSteel=
//        kotlinFileCreator.create(
//            fileText = kotlinFileCreator.fileText.replace("ItemType.Object", "ItemType.OBJECT").replace("ByRefProgDet","ByRef ProgDet"),
//            fileName = nomeClasse,
//            saveFolderPath = enderecoParaSalvarArquivoKotlin
//        )
//    }
}