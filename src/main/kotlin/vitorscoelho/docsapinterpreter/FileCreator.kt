package vitorscoelho.docsapinterpreter

import java.io.File

class FileCreator(val fileInit: String, val fileEnd: String, val docFolder: File) {
    val fileText: String = let {
        val stringBuilder = StringBuilder()
        stringBuilder.append(fileInit)
        stringBuilder.append("\r\n")
        docFolder.listFiles().forEach { file ->
            println("Função: ${file.name}")
            if (file.isFile) {
                stringBuilder.append(SAPDoc(file.readText()).kotlinText)
                stringBuilder.append("\r\n")
            }
        }
        stringBuilder.append("\r\n")
        stringBuilder.append(fileEnd)
//        println(stringBuilder.toString())
        stringBuilder.toString()
    }

    fun create(
        fileText: String = this.fileText,
        fileName: String = docFolder.nameWithoutExtension,
        saveFolderPath: String
    ) {
        File("$saveFolderPath\\$fileName.kt").writeText(fileText)
    }
}