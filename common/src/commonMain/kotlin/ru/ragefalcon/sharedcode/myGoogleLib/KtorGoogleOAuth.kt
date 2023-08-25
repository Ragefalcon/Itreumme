package ru.ragefalcon.sharedcode.myGoogleLib

import com.soywiz.klock.DateTimeTz
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.source.disk.CommonName
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.source.disk.FileMP
import ru.ragefalcon.sharedcode.source.disk.getCorDisp

@Parcelize
class ItemKtorGoogleParams(
    var redirectURI: String = "",
    var authCode: String = "",
    var clientID: String = "",
    var clientSecret: String = "",
    var access_token: String = "",
    var refresh_token: String = "",
    var expires_in: Int = -1,
    var lastTimeCode: Long = 0L
) : Id_class(id_main = authCode) {
}

@Parcelize
class ItemGDriveFile(
    var kind: String = "",
    var id: String = "",
    var name: String = "",
    var mimeType: String = ""
) : Id_class(id_main = id) {
}

class KtorGoogleOAuth(
    val dbArgs: DbArgs,
    val params: ItemKtorGoogleParams = ItemKtorGoogleParams(),
    val networkFilesDir: DbArgs? = null,
    private val pushParams: (ItemKtorGoogleParams) -> Unit = {}
) {
    var pushListDriveFile: (List<ItemGDriveFile>) -> Unit = {}

    /**
     * Если оставить движок по умолчанию, а не выбрать CIO, то при загрузке файлов
     * пареметр contentLength() будет выдаваться null. Возможно проблему решит и
     * какой-нибудь другой движок, но в официальном примере/инструкции для ktor
     * был указан именно CIO
     * */
    val client = HttpClient(CIO) {

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                isLenient = false
                ignoreUnknownKeys = false
                useArrayPolymorphism = false
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    for (line in message.lines())
                        println("KtorLog: ${line.trim()}")
                }
            }
            level = LogLevel.ALL
        }

        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(response)
                    in 400..499 -> throw ClientRequestException(response)
                    in 500..599 -> throw ServerResponseException(response)
                }

                if (statusCode >= 600) {
                    throw ResponseException(response)
                }
            }
        }
        defaultRequest {
            accept(ContentType.Application.Json)
        }
    }

    fun getToken(
        code: String,
        clientID: String,
        clientSecret: String,
        redirectURI: String = "",
        ff: (String) -> Unit = {}
    ) {
        if (params.authCode != code) {
            params.authCode = code
            params.clientID = clientID
            params.clientSecret = clientSecret
            params.redirectURI = redirectURI
            CoroutineScope(Dispatchers.Default).launch {

                /**
                 * https://stackoverflow.com/questions/23959987/youtube-api-v3-error-ssl-is-required-to-perform-this-operation
                 * при ошибке 403 Forbiden связанной с SSL нужно просто жестко указать https в ссылке, если пытаться вписать https в host,
                 * то вылезает ошибка, возможно это можно указать где то еще, но работает и просто в ссылке
                 *
                 * А еще добавляемые парметры нужно писать как есть, а не как ссылку в браузере,
                 * т.е. http://127.0.0.1:5000, а не http%3A//127.0.0.1%3A5000 (т.е. не править кодировку)
                 * */
                try {
                    val respon = client.request<JsonObject>("https://oauth2.googleapis.com/token") {
                        parameter("code", code)
                        parameter("redirect_uri", redirectURI)
                        parameter("client_id", clientID)
                        parameter("client_secret", clientSecret)
                        parameter("grant_type", "authorization_code")
                        method = HttpMethod.Post
                    }
                    var tmp = respon["access_token"].toString()
                    params.access_token = tmp.subSequence(1, tmp.length - 1).toString()
                    tmp = respon["refresh_token"].toString()
                    params.refresh_token = tmp.subSequence(1, tmp.length - 1).toString()
                    params.expires_in = respon["expires_in"].toString().toInt()
                    params.lastTimeCode = DateTimeTz.nowLocal().localUnix()
                    CoroutineScope(getCorDisp()).launch {
                        pushParams(params)
                        val tokstr = code + "|" +
                                params.access_token + "|" +
                                params.refresh_token + "|" +
                                params.expires_in.toString() + "|" +
                                params.lastTimeCode.toString()
                        ff(tokstr)
                    }

                } catch (exception: Exception) {
                    println("Ktor, exception: ${exception.message}")
                }


            }
        }
    }

    fun refreshToken(
        ff: (Boolean) -> Unit = {}
    ) {
        if (params.authCode != "" && params.refresh_token != "") {
            CoroutineScope(getCorDisp()).launch {
                try {
                    val respon = client.request<JsonObject>("https://oauth2.googleapis.com/token") {
                        parameter("client_id", params.clientID)
                        parameter("client_secret", params.clientSecret)
                        parameter("refresh_token", params.refresh_token)
                        parameter("grant_type", "refresh_token")
                        method = HttpMethod.Post
                    }
                    val tmp = respon["access_token"].toString()
                    params.access_token = tmp.subSequence(1, tmp.length - 1).toString()
                    params.expires_in = respon["expires_in"].toString().toInt()
                    params.lastTimeCode = DateTimeTz.nowLocal().localUnix()

                    pushParams(params)
                    ff(true)
                } catch (exception: Exception) {
                }
            }
        }
    }

    fun revokeRefreshToken(
        ff: (Boolean) -> Unit = {}
    ) {
        if (params.authCode != "" && params.refresh_token != "") {
            CoroutineScope(getCorDisp()).launch {
                try {
                    val respon = client.request<JsonObject>("https://oauth2.googleapis.com/revoke") {
                        parameter("token", params.refresh_token)
                        method = HttpMethod.Post
                    }
                    params.refresh_token = ""
                    params.expires_in = 0
                    params.lastTimeCode = DateTimeTz.nowLocal().localUnix()
                    pushParams(params)
                    ff(true)
                } catch (exception: Exception) {
                }


            }
        }
    }

    fun revokeAccessToken(
        ff: (Boolean) -> Unit = {}
    ) {
        if (params.authCode != "" && params.refresh_token != "") {
            CoroutineScope(getCorDisp()).launch {
                try {
                    val respon = client.request<JsonObject>("https://oauth2.googleapis.com/revoke") {
                        parameter("token", params.access_token)
                        method = HttpMethod.Post
                    }
                    params.access_token = ""
                    params.expires_in = 0
                    params.lastTimeCode = DateTimeTz.nowLocal().localUnix()
                    pushParams(params)
                    ff(true)
                } catch (exception: Exception) {
                    println("Ktor, exception: ${exception.message}")
                }


            }
        }
    }

    fun downloadFile(id: String, prog: (Float) -> Unit = {}) {
        CoroutineScope(getCorDisp()).launch {

            val aa = client.get<HttpStatement>("https://www.googleapis.com/drive/v3/files/$id?alt=media") {
                headers.append("Authorization", "Bearer ${params.access_token}")
                headers.append("Accept", "application/json")
                parameter("spaces", "appDataFolder")
            }.execute { httpResponse ->
                val byteBufferSize = 1024 * 100
                val byteBuffer = ByteArray(byteBufferSize)
                var count = 0
                val bbb = httpResponse.contentLength()
                val channel: ByteReadChannel = httpResponse.receive()
                val fileLoad = FileMP()
                fileLoad.openFileOutput(networkFilesDir ?: dbArgs, CommonName.nameFromNetworkDBfile)
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(4096)
                    while (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        count++
                        fileLoad.writeFile(bytes)
                        httpResponse.contentLength()?.let { length ->
                            prog((httpResponse.content.totalBytesRead) / length.toFloat())
                        }
                        println(
                            "Ktor: Received ${count * 4096} bytes from ${bbb} " +
                                    "${httpResponse.contentLength()} -> ${httpResponse.content.totalBytesRead}"
                        )
                    }
                }
                fileLoad.closeFile()
                prog(1F)
                println("Ktor: A file saved to ${count}")
            }
        }
    }

    suspend fun uploadFile(pathSource: String, nameUpF: String, prog: (Float) -> Unit = {}) {
        try {
            val fileLoad = FileMP()
            fileLoad.openFileInput(dbArgs, pathSource)
            fileLoad.getFileStream()?.let { inputF ->
                val response: HttpResponse = client.submitFormWithBinaryData(
                    url = "https://www.googleapis.com/upload/drive/v3/files",
                    formData = formData {
                        val meta = JsonObject(
                            mapOf(
                                "name" to JsonPrimitive("$nameUpF.db"),
                                "mimeType" to JsonPrimitive("text/plain"),
                                "parents" to JsonArray(listOf(JsonPrimitive("appDataFolder")))
                            )
                        )
                        append("metadata", meta.toString().toByteArray(),
                            Headers.build {
                                this["Content-Type"] = "application/json"
                            })
                        append("body", inputF,
                            Headers.build {
                                this["Content-Type"] = "text/plain"
                            })
                    }
                ) {
                    parameter("uploadType", "multipart")
                    headers {
                        this[HttpHeaders.Authorization] = "Bearer ${params.access_token}"
                    }
                    onUpload { bytesSentTotal, contentLength ->
                        prog(bytesSentTotal / contentLength.toFloat())
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    suspend fun overwriteFile(pathSource: String, id: String, prog: (Float) -> Unit = {}) {
        try {
            val fileLoad = FileMP()
            fileLoad.openFileInput(dbArgs, pathSource)
            fileLoad.getFileStream()?.let { inputF ->
                val response = client.patch<HttpResponse>("https://www.googleapis.com/upload/drive/v3/files/$id") {
                    headers {
                        this[HttpHeaders.Authorization] = "Bearer ${params.access_token}"
                        this["Content-Type"] = "text/plain"
                    }
                    body = inputF
                    onUpload { bytesSentTotal, contentLength ->
                        prog(bytesSentTotal / contentLength.toFloat())
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    suspend fun createFolder(name: String) {
        try {
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = "https://www.googleapis.com/upload/drive/v3/files",
                formData = formData {
                    val meta = JsonObject(
                        mapOf(
                            "name" to JsonPrimitive("$name"),
                            "mimeType" to JsonPrimitive("application/vnd.google-apps.folder"),
                            "parents" to JsonArray(listOf(JsonPrimitive("appDataFolder")))
                        )
                    )
                    append("metadata", meta.toString().toByteArray(),
                        Headers.build {
                            this["Content-Type"] = "application/json"
                        })
                }
            ) {
                headers {
                    this[HttpHeaders.Authorization] = "Bearer ${params.access_token}"
                }
                onUpload { bytesSentTotal, contentLength ->
                }
            }
        } catch (e: Exception) {
        }
    }

    suspend fun deleteFile(id: String) {
        try {
            val response: HttpResponse = client.delete("https://www.googleapis.com/drive/v3/files/$id") {
                headers {
                    this[HttpHeaders.Authorization] = "Bearer ${params.access_token}"
                }
            }
        } catch (e: Exception) {
        }
    }


    fun GetAppFilesList(ff: (String) -> Unit = {}) {
        CoroutineScope(Dispatchers.Default).launch {
            try {

                val respon = client.request<JsonObject>("https://www.googleapis.com/drive/v3/files") {
                    headers.append("Authorization", "Bearer ${params.access_token}")
                    headers.append("Accept", "application/json")
                    parameter("spaces", "appDataFolder")
                    method = HttpMethod.Get
                }

                if (!respon.isNullOrEmpty()) {
                    val aa = respon.toList()
                    val listFile = mutableListOf<ItemGDriveFile>()
                    aa.find { it.first == "files" }?.second?.let {
                        for (i in it.jsonArray) {
                            listFile.add(
                                ItemGDriveFile(
                                    kind = i.getStrPrimByName("kind"),
                                    id = i.getStrPrimByName("id"),
                                    name = i.getStrPrimByName("name"),
                                    mimeType = i.getStrPrimByName("mimeType")
                                )
                            )
                        }
                    }
                    var rez = ""
                    for (i in listFile) rez += i.name + "\n"
                    pushListDriveFile(listFile)
                    ff(rez)
                }
            } catch (exception: Exception) {
                println("Ktor, exception: ${exception.message}")
            }
        }
    }

    fun minusKav(str: String): String {
        return str.subSequence(1, str.length - 1).toString()
    }

    fun minusKav(element: JsonElement): String {
        var str = element.toString()
        return str.subSequence(1, str.length - 1).toString()
    }
}

fun JsonElement.getStrPrimByName(name: String): String {
    val str = this.jsonObject[name].toString()
    return str.subSequence(1, str.length - 1).toString()
}