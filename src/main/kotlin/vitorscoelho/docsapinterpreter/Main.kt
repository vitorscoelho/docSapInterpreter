package vitorscoelho.docsapinterpreter

import java.io.File

fun main() {
    val caminhoPasta =
        "C:\\Users\\vitor\\Downloads\\API_Sap2000v14\\SAP2000 v14 documentation\\SAP2000_API_Fuctions\\Definitions\\Properties\\Area\\"
    File(caminhoPasta).listFiles().forEachIndexed { index, file ->
        if (file.isFile ) {
            SAPDoc(file.readText())
        }
    }
    println("Total de funções = ${File(caminhoPasta).listFiles().filter { it.isFile }.size}")
    /*arrayListOf(
        "ApplicationExit.htm",
        "ApplicationStart.htm",
        "JointDispl.htm",
        "JointDisplAbs.htm",
        "JointReact.htm"
    ).forEach {
        SAPDoc(File("$caminhoPasta$it").readText())
    }*/
}