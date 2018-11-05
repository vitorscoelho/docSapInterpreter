package vitorscoelho.docsapinterpreter

import java.io.File

fun main(){
    val caminhoPasta = "C:\\Users\\vitor\\Downloads\\API_Sap2000v14\\SAP2000 v14 documentation\\SAP2000_API_Fuctions\\Definitions\\Load_Case\\"
    arrayListOf("Delete_{Load_Case}.htm").forEach {
        SAPDoc(File("$caminhoPasta$it").readText())
    }
}