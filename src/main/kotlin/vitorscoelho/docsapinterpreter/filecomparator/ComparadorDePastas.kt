package vitorscoelho.docsapinterpreter.filecomparator

import vitorscoelho.docsapinterpreter.SAPDoc
import java.io.File
import java.lang.Exception

class Pasta(val file: File) {
    val subpastas: List<File> = getSubpastas(file = file)
    val arquivos: List<File> = getArquivos(pasta = file, subpastas = subpastas)


    companion object {
        fun getSubpastas(file: File): List<File> {
            if (!file.isDirectory) return emptyList()
            val subpastas = arrayListOf<File>()
            file.listFiles().filter { it.isDirectory }.forEach { subpasta ->
                subpastas += subpasta
                subpastas += getSubpastas(subpasta)
            }
            return subpastas
        }

        fun getArquivos(pasta: File, subpastas: List<File>): List<File> {
            val todasPastas = subpastas + pasta
            val arquivos = arrayListOf<File>()
            todasPastas.forEach {
                arquivos += it.listFiles { arquivo -> arquivo.isFile }
            }
            return arquivos
        }

        fun getArquivos(pasta: File): List<File> = getArquivos(pasta = pasta, subpastas = getSubpastas(file = pasta))
    }
}

class ComparadorDePastas(val pasta1: Pasta, val pasta2: Pasta) {
    val arquivosExclusivosPasta1: List<File>
    val arquivosExclusivosPasta2: List<File>
    val subpastasExclusivasPasta1: List<File>
    val subpastasExclusivasPasta2: List<File>
    val arquivosComMesmoNomeMasComConteudoDiferente: List<File>

    init {
        val enderecosArquivosPasta1 = pasta1.arquivos.map { it.relativeTo(base = pasta1.file) }
        val enderecosArquivosPasta2 = pasta2.arquivos.map { it.relativeTo(base = pasta2.file) }
        val enderecosSubpastasPasta1 = pasta1.subpastas.map { it.relativeTo(base = pasta1.file) }
        val enderecosSubpastasPasta2 = pasta2.subpastas.map { it.relativeTo(base = pasta2.file) }

        this.arquivosExclusivosPasta1 =
                listaExclusivos(
                    enderecosOriginais = enderecosArquivosPasta1,
                    enderecosAExcluir = enderecosArquivosPasta2,
                    pastaRaizOriginal = pasta1
                )
        this.arquivosExclusivosPasta2 =
                listaExclusivos(
                    enderecosOriginais = enderecosArquivosPasta2,
                    enderecosAExcluir = enderecosArquivosPasta1,
                    pastaRaizOriginal = pasta2
                )

        this.subpastasExclusivasPasta1 =
                listaExclusivos(
                    enderecosOriginais = enderecosSubpastasPasta1,
                    enderecosAExcluir = enderecosSubpastasPasta2,
                    pastaRaizOriginal = pasta1
                )
        this.subpastasExclusivasPasta2 =
                listaExclusivos(
                    enderecosOriginais = enderecosSubpastasPasta2,
                    enderecosAExcluir = enderecosSubpastasPasta1,
                    pastaRaizOriginal = pasta2
                )

        val enderecosArquivosQueExistemNasDuasPastas =
            enderecosArquivosPasta1.filter { enderecosArquivosPasta2.contains(it) }
        this.arquivosComMesmoNomeMasComConteudoDiferente = enderecosArquivosQueExistemNasDuasPastas.filter {
            val filePasta1 = File("${pasta1.file.absolutePath}/$it")
            val filePasta2 = File("${pasta2.file.absolutePath}/$it")
            try {
                val sapDoc1 = SAPDoc(filePasta1.readText())
                val sapDoc2 = SAPDoc(filePasta2.readText())
                sapDoc1.kotlinText != sapDoc2.kotlinText
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun listaExclusivos(
        enderecosOriginais: List<File>,
        enderecosAExcluir: List<File>,
        pastaRaizOriginal: Pasta
    ): List<File> {
        return (enderecosOriginais - enderecosAExcluir)
            .map { File("${pastaRaizOriginal.file.absolutePath}/$it") }
    }

    private fun arquivosComMesmoNomeELocalizacao(
        arquivo1: File,
        arquivo2: File,
        pastaRaizArquivo1: File,
        pastaRaizArquivo2: File
    ): Boolean {
        val enderecoRelativo1 = arquivo1.absolutePath.removePrefix(pastaRaizArquivo1.absolutePath)
        val enderecoRelativo2 = arquivo2.absolutePath.removePrefix(pastaRaizArquivo2.absolutePath)
        return enderecoRelativo1 == enderecoRelativo2
    }
}