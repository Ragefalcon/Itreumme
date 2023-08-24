package GooglePack

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import viewmodel.StateVM

var sess: NettyApplicationEngine? = null
fun mainFF() {
    if (sess == null) {
        sess = embeddedServer(Netty, port = 5000) {
            install(WebSockets)
            routing {
                get("/") {

                    call.respondText(
                        "Поздравляем, вы успешно авторизовались, можете закрыть вкладку и вернуться в приложение."
                    )
                    this.context.parameters["code"]?.let {
                        /**
                         * про
                         * Platform.runLater()
                         * смотреть здесь
                         * https://stackoverflow.com/questions/29449297/java-lang-illegalstateexception-not-on-fx-application-thread-currentthread-t
                         * кратко: таким образом можно выполнить задачу в осноном потоке из неосновного зацикленного потока
                         * */
                        CoroutineScope(Dispatchers.Default).launch {
                            StateVM.getMyAuthCode(it)
                        }
                    }
                }
            }
        }.start(wait = false)
    }
}