package ru.ragefalcon.sharedcode.extensions



@Parcelize
data class MyColorFloatARGB(
    var A:Float,
    var R:Float,
    var G:Float,
    var B:Float
): Parcelable {
    fun toMyIntCol():MyColorARGB{

        return MyColorARGB(
            (this.A*255).toInt(),
            (this.R*255).toInt(),
            (this.G*255).toInt(),
            (this.B*255).toInt()
        )
    }
    fun plusWhite():MyColorFloatARGB{
        return MyColorFloatARGB(
            R = if ((R + (1.0 - R)/2.0).toFloat()<=1) (R + (1.0 - R)/2.0).toFloat() else 1.0.toFloat(),
            G = if ((G + (1.0 - G)/2.0).toFloat()<=1) (G + (1.0 - G)/2.0).toFloat() else 1.0.toFloat(),
            B = if ((B + (1.0 - B)/2.0).toFloat()<=1) (B + (1.0 - B)/2.0).toFloat() else 1.0.toFloat(),
            A = A
        )
    }

}
private fun checkHexaDecimalNumber(hexaDecimalNum: String): Boolean {
    var isHexaDecimalNum = true

    for(charAtPos in hexaDecimalNum) {
        if(!(
                    ((charAtPos >= '0') && (charAtPos <= '9'))
                            || ((charAtPos >= 'A') && (charAtPos <= 'F'))
                            || ((charAtPos >= 'a') && (charAtPos <= 'f'))
                    )) {
            isHexaDecimalNum = false
            break
        }
    }
    return isHexaDecimalNum
}

@Parcelize
data class MyColorARGB(
    var A:Int,//Range(from = 0, to = 255),
    var R:Int,
    var G:Int,
    var B:Int
): Parcelable {
    companion object {
        val DOXODDARKGREEN = MyColorARGB("FF237700")
        val MYBEG = MyColorARGB("FFFFF7D9")
        val colorRasxodItem0 = MyColorARGB("FFFFD9E5")
        val colorRasxodItem = MyColorARGB("FFFFF1F5")
        val colorRasxodTheme0 = MyColorARGB("FFFF8800")
        val colorRasxodTheme = MyColorARGB("FFFFE2C1")
        val colorDoxodItem0 = MyColorARGB("FFDAFFD9")
        val colorDoxodItem = MyColorARGB("FFEBFFEB")
        val colorDoxodItemText = MyColorARGB("FF059C00")
        val colorDoxodTheme0 = MyColorARGB("FF44991F")
        val colorDoxodTheme = MyColorARGB("FFCEE1C6")
        val colorSchetItem0 = MyColorARGB("FFFFF7D9")
        val colorSchetItem = MyColorARGB("FFFFFAE6")
        val colorSchetItemText = MyColorARGB("FF9C7500")
        val colorSchetTheme0 = MyColorARGB("FF99851F")
        val colorSchetTheme = MyColorARGB("FFE4E0C7")
        val colorMyMainTheme = MyColorARGB("FF464D45")
        val colorMyAppBarDesktop = MyColorARGB("FF614831")
        val colorStatTimeSquareTint_00 = MyColorARGB("FFFFF42B")
        val colorStatTimeSquareTint_01 = MyColorARGB("FFFFFFFF")
        val colorStatTimeSquareTint_02 = MyColorARGB("FF7FFAF6")
        val colorStatTimeSquareTint_03 = MyColorARGB("FFFF5858")
        val colorStatTint_00 = MyColorARGB("FFFFFFFF")
        val colorStatTint_01 = MyColorARGB("FF2FA61D")
        val colorStatTint_02 = MyColorARGB("FF7FFAF6")
        val colorStatTint_03 = MyColorARGB("FFFFF42B")
        val colorStatTint_04 = MyColorARGB("FFFFA825")
        val colorStatTint_05 = MyColorARGB("FFFF5858")
        val colorEffektShkal_Nedel = MyColorARGB("FF89B62D")
        val colorEffektShkal_Month = MyColorARGB("FF2DB82D")
        val colorEffektShkal_Year = MyColorARGB("FF82D882")
        val colorEffektShkal_Back = MyColorARGB("FFDAA4AE")
        val colorEffektShkal_BackRed = MyColorARGB("FFE0573E")
        val colorBackRamk = MyColorARGB("FFACB8A5")
        val colorBackGr1 = MyColorARGB("FF728766")
        val colorBackGr2 = MyColorARGB("FF576350")
        val colorBackGr3 = MyColorARGB("FF31372D")
        val TreeSkillCloseEdit = MyColorARGB("FFad7b36")
        val colorCompleteStapPlan = MyColorARGB("FF468F45")
        val colorUnblockNowElement = MyColorARGB("FF86CF85")
        val colorBlockInvisElement = MyColorARGB("FF264F25")
        val colorSleep = MyColorARGB("FF9390c5")
        val colorMyBorderStrokeCommon = MyColorARGB("8FFFF7D9")
        val colorMyBorderStrokeLite = MyColorARGB("4FFFF7D9")
        val colorMyBorderStroke = MyColorARGB("fFFFF7D9")
        val colorTransparent = MyColorARGB("00000000")
    }

    constructor(fff: String):this(0,0,0,0) {
        if (checkHexaDecimalNumber(fff)) {

            fun mapHex(charM: Char): Int {
                return when (charM) {
                    '0' -> 0
                    '1' -> 1
                    '2' -> 2
                    '3' -> 3
                    '4' -> 4
                    '5' -> 5
                    '6' -> 6
                    '7' -> 7
                    '8' -> 8
                    '9' -> 9
                    'a' -> 10
                    'b' -> 11
                    'c' -> 12
                    'd' -> 13
                    'e' -> 14
                    'f' -> 15
                    'A' -> 10
                    'B' -> 11
                    'C' -> 12
                    'D' -> 13
                    'E' -> 14
                    'F' -> 15
                    else -> 0
                }
            }
            var ff = fff
            if (ff.length<6){
                for (ii in 1..6-ff.length){
                    ff = "0$ff"
                }
            }
            val i = ff.length - 1

            this.B = mapHex(ff[i-1])*16+mapHex(ff[i])
            this.G = mapHex(ff[i-3])*16+mapHex(ff[i-2])
            this.R = mapHex(ff[i-5])*16+mapHex(ff[i-4])
            if (i == 7){
                this.A = mapHex(ff[i-7])*16+mapHex(ff[i-6])
            }   else  {
                this.A = 255
            }
//            println("this.A : ${this.A}")
//            println("this.R : ${this.R}")
//            println("this.G : ${this.G}")
//            println("this.B : ${this.B}")
        }
    }
    fun toFloatCol():MyColorFloatARGB{
        return MyColorFloatARGB(
            this.A/255.0F,
            this.R/255.0F,
            this.G/255.0F,
            this.B/255.0F
        )
    }

    private fun hexOne(int: Int):String = when (int%16) {
        0 -> "0"
        1 -> "1"
        2 -> "2"
        3 -> "3"
        4 -> "4"
        5 -> "5"
        6 -> "6"
        7 -> "7"
        8 -> "8"
        9 -> "9"
        10 -> "A"
        11 -> "B"
        12 -> "C"
        13 -> "D"
        14 -> "E"
        15 -> "F"
        else -> "0"
    }
    fun toHexString(): String  =
            hexOne(A/16) +
            hexOne(A%16) +
            hexOne(R/16) +
            hexOne(R%16) +
            hexOne(G/16) +
            hexOne(G%16) +
            hexOne(B/16) +
            hexOne(B%16)

    fun inColor(target: MyColorARGB, percent: Float): MyColorARGB{
        return MyColorARGB(
            this.A + ((target.A - this.A)*percent).toInt(),
            this.R + ((target.R - this.R)*percent).toInt(),
            this.G + ((target.G - this.G)*percent).toInt(),
            this.B + ((target.B - this.B)*percent).toInt())
    }
    fun plusWhite(koef: Float = 2F):MyColorARGB{
        return MyColorARGB(
            this.A,
            this.R + ((255 - this.R)/koef).toInt(),
            this.G + ((255 - this.G)/koef).toInt(),
            this.B + ((255 - this.B)/koef).toInt())
//        R = R + (255 - R)/2
//        G = G + (255 - G)/2
//        B = B + (255 - B)/2
//        return this
    }
    fun plusDark(koef: Float = 0.95F):MyColorARGB{
        return MyColorARGB(
            this.A,
            (R*koef).toInt(),
            (G*koef).toInt(),
            (B*koef).toInt())
//        R = (R*0.95).toInt()
//        G = (G*0.95).toInt()
//        B = (B*0.95).toInt()
//        return this
    }

}


fun myColorRaduga(pos: Int): MyColorARGB
{
    var R: Int
    var G: Int
    var B: Int
    R=0; G=0; B=0;
    var col=pos%1024;
    var sw=col/256;
    when (sw) {
         0 -> {
             R=255
             G=col%256
             B=0 }
         1 -> {
             R=255-col%256
             G=0
             B=255 }
         2 -> {
             R=0
             G=255
             B=col%256 }
         3 -> {
             R=128
             G=255-col%256
             B=128 }
    }

    return MyColorARGB(255,R,G,B)
}
fun myColorRadugaFloat(pos: Int): MyColorFloatARGB {
    return myColorRaduga(pos).toFloatCol()
}


//---------------------------------------------------------------------------
