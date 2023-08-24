package common

import org.junit.Assert.*
import org.junit.Test

class testTest{
    val str =
"sda234ksdf,896d234af587.23.fsd24.23487.86.saf2345"
//    val str =
//"sda234ksdf896d234af58723fsd24234878667asdf2434"
    val reg = Regex("[0-9]+")

    fun customStrToDouble(str: String): String {
        val matchedResults = Regex(pattern = """[0-9,.]+""").findAll(input = str)
        val result = StringBuilder()
        for (matchedText in matchedResults) {
            result.append(matchedText.value + "")
        }
        var rez = ",".toRegex().replace(result,".")
        for (i in 1 until "\\.".toRegex().findAll(rez).count()){
            rez = "\\.".toRegex().replaceFirst(rez,"")
        }
        rez = "[0-9]+\\.?[0-9]?[0-9]?".toRegex().find(rez,0)?.value ?: ""
//        rez = rez.trim('0')
//        if (rez.length>0 && rez[0] != '.')
        return rez
    }

    fun customStrToInt(str: String): String {
        val matchedResults = Regex(pattern = """[0-9]+""").findAll(input = str)
        val result = StringBuilder()
        for (matchedText in matchedResults) {
            result.append(matchedText.value + "")
        }
        return result.toString()
    }
    @Test
    fun safsa2() {
        println(customStrToDouble("0000.32324"))
    }

    @Test
    fun safsa(){
        val curcorIn = 48-2
        var strCurIn = ""
        for (i in 1 until curcorIn) strCurIn += " "
        strCurIn += "^"
        var rez = ",".toRegex().replace(str,".")
        val str1 = rez.substring(0,curcorIn)
        val str2 = rez.substring(curcorIn,rez.length)
//        val matchedResults = Regex(pattern = """[0-9||,||.]+""").findAll(input = rez)
//        val result = StringBuilder()
//        for (matchedText in matchedResults) {
//            result.append(matchedText.value + "")
//        }
//        for (i in 1 until "\\.".toRegex().findAll(rez).count()){
//            rez = "\\.".toRegex().replaceFirst(rez,"")
//        }
//        rez = "[0-9]+\\.?[0-9]?[0-9]?".toRegex().find(rez,0)?.value ?: ""

        rez = customStrToDouble(str)

        val checkPoint1 = "\\.".toRegex().containsMatchIn(str1)
        val checkPoint2 = "\\.".toRegex().containsMatchIn(str2)

        var cursorOut = 0
        if (checkPoint2){
            cursorOut = customStrToInt(str1).length
        } else if (checkPoint1){
            cursorOut = customStrToDouble(str1).length
        }   else {
            cursorOut = customStrToInt(str1).length
        }
        val strR1 = rez.substring(0,cursorOut)
        val strR2 = rez.substring(cursorOut,rez.length)

        var cursorInvIn = 10
        val strInv1 = rez.substring(0,cursorInvIn)
        val strInv2 = rez.substring(cursorInvIn,rez.length)

        val checkPointInv1 = "\\.".toRegex().containsMatchIn(strInv1)
        val checkPointInv2 = "\\.".toRegex().containsMatchIn(strInv2)

        var cursorInvOut = 0
        if (checkPointInv2){
            cursorInvOut = customStrToInt(str1).length
        } else if (checkPointInv1){
            cursorInvOut = customStrToDouble(str1).length
        }   else {
            cursorInvOut = customStrToInt(str1).length
        }
        println("rez: $str")
        println("rez: $str1^$str2")
        println("rez: $rez")
        println("rez: $strR1^$strR2")
        println("curcorIn && Out = ${curcorIn} && $cursorOut")
        println()
//        println("rez: ${"[0-9]+\\.[0-9][0-9]".toRegex().matchAt(rez,0)}")
//        println("rez: ${reg.matchEntire("12341f234")?.value}")
    }
}