package GooglePack

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                        CoroutineScope(Dispatchers.Default).launch {
                            StateVM.getMyAuthCode(it)
                        }
                    }
                }
            }
        }.start(wait = false)
    }
}