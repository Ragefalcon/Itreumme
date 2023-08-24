package common.tests

import java.io.File

fun generateName() {
    class bukva(
        val num: Int,
        val char: String,
        val soglas: Boolean
    )
    val iter = arrayOf(
        bukva(1,"i",false),
        bukva(2,"t",true),
        bukva(3,"e",false),
        bukva(4,"r",true),
        bukva(5,"m",true),
        bukva(6,"e",false),
        bukva(7,"u",false),
        bukva(8,"m",true),
    )
    val f = File(System.getProperty("user.dir"), "anagramm.txt") //+ "\\MyGrids"
    f.printWriter().use { out ->
        var numWord = 0
//        var soglas = 0
        var countTwoBukv = 0
        var countWord = 0
        fun recur(soglas: Int, word: Array<bukva>){
            if (word.count() == 8){
                numWord++
                countWord++
                for (b in word) {
                    out.print(b.char)
                }
                out.print(" ")
                if (numWord==6) {
                    out.println()
                    numWord = 0
                }
            } else {
                for (buk in iter.filter { !word.contains(it) }) {
                    var sogl = soglas
                    if (buk.soglas) {
                        if (sogl<0) sogl = 0
                        sogl++
                    } else {
                        if (sogl>0) sogl = 0
                        sogl--
                    }
                    if (sogl*sogl<10) {
                        if (word.count() == 2) {
                            countTwoBukv++
                            if ("${word[0].char}${word[1].char}"!="mm") {
                                println("countTwoBukv = ${countTwoBukv} : ${word[0].char}${word[1].char}")
                                recur(sogl,mutableListOf<bukva>().apply { addAll(word); add(buk) }.toTypedArray())
                            }
                        }   else    {
                            recur(sogl,mutableListOf<bukva>().apply { addAll(word); add(buk) }.toTypedArray())
                        }
                    }   else    {
                        println("sogl = ${sogl}")
                    }
                }
            }
        }
        recur(0,mutableListOf<bukva>().toTypedArray())
        println("countWord = ${countWord}")
    }
//    f.bufferedWriter().write("asdfasdf")
//    f.bufferedWriter().write("22342314")
}