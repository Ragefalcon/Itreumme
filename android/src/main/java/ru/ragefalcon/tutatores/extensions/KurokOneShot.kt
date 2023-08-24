package ru.ragefalcon.tutatores.extensions

class KurokEndless {
    private var kurok = true

    fun skip(){kurok = false}

    fun fire(firefun: ()->Unit){
        if (kurok) firefun() else kurok = true
    }
}
class KurokOneShot {
    private var kurok = false

    fun vzvesti(){kurok = true}

    private var fireFun: (()->Unit)? = null

    fun setFire(firefun: ()->Unit) {
        fireFun = firefun
    }

    fun fire(firefun: ()->Unit){
        if (kurok) {
            kurok = false
            firefun()
        }
    }

    fun fire(){
        fireFun?.let {
            it.invoke()
            fireFun = null
        }
    }

}