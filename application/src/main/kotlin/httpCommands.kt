//import java.net.URI
//import java.net.http.HttpClient
//import java.net.http.HttpRequest
//import java.net.http.HttpResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject

val BACKEND_URL_REMOTE = "http://personal-organizer.petarvico.com:8080/"
val BACKEND_URL_LOCAL = "http://0.0.0.0:8080/"
val BACKEND_URL = BACKEND_URL_REMOTE

// Custom Http Class
class MyHttp {

    // print request and response to console for debugging
    suspend fun printRequestAndResponse(request: String, response: HttpResponse, body: String = "")  {
        println("Request: ${request}\n${body}")
        println("Response: ${response.status}\n" +
                "BODY: ${response.body<String>()}\n\n\n")
    }

    // GET method
    suspend fun get(endpoint: String) : HttpResponse {
        val client = HttpClient(CIO)
        val response = client.get(BACKEND_URL + endpoint)
        printRequestAndResponse("GET ==> ${BACKEND_URL + endpoint}", response)
        return response
    }

    // POST method
    suspend fun post(endpoint: String, jsonMap: JsonObject) : HttpResponse {
        val client = HttpClient(CIO);
        val response : HttpResponse = client.post(BACKEND_URL + endpoint) {
            contentType(ContentType.Application.Json)
            setBody(jsonMap.toString())
        }
        printRequestAndResponse("POST ==> ${BACKEND_URL + endpoint}", response, jsonMap.toString())
        return response
    }

    // PUT method
    suspend fun put(endpoint: String, jsonMap: JsonObject) : HttpResponse {
        val client = HttpClient(CIO);
        val response : HttpResponse = client.put(BACKEND_URL + endpoint) {
            contentType(ContentType.Application.Json)
            setBody(jsonMap.toString())
        }
        printRequestAndResponse("POST ==> ${BACKEND_URL + endpoint}", response, jsonMap.toString())
        return response
    }

    // DELETE method
    suspend fun delete(endpoint: String, jsonMap: Map<String, String>) : HttpResponse {
        var deleteUrl = BACKEND_URL + endpoint + "?"
        for ((key, value) in jsonMap) {
            deleteUrl += key + "=" + value + "&"
        }
        val client = HttpClient(CIO);
        val response : HttpResponse = client.delete(deleteUrl)
        printRequestAndResponse("DELETE ==> ${BACKEND_URL + endpoint}", response)
        return response
    }

}

