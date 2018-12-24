package vitorscoelho.docsapinterpreter

import java.io.File

fun main() {
    val caminhoPasta="C:\\Users\\vitor\\IdeaProjects\\sw4k\\src\\main\\kotlin\\vitorscoelho\\sw4k\\sapversions\\v14\\sapmodel\\design\\coldformed"
    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
        println("val ${file.nameWithoutExtension}:${file.nameWithoutExtension}V14")
    }
    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
        println("override val ${file.nameWithoutExtension}=${file.nameWithoutExtension}(programName)")
    }
//    val caminhoPasta =
//        "C:\\Users\\vitor\\Downloads\\API_Sap2000v14\\SAP2000 v14 documentation\\SAP2000_API_Fuctions\\Design\\Concrete\\"
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