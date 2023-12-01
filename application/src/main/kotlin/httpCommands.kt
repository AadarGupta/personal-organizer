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

class MyHttp {

    suspend fun printRequestAndResponse(request: String, response: HttpResponse, body: String = "")  {
        println("Request: ${request}\n${body}")
        println("Response: ${response.status}\n" +
                "BODY: ${response.body<String>()}\n\n\n")
    }

    suspend fun get(endpoint: String) : HttpResponse {
        val client = HttpClient(CIO)
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create())
//            .GET()
//            .build()
        val response = client.get(BACKEND_URL + endpoint)
        printRequestAndResponse("GET ==> ${BACKEND_URL + endpoint}", response)
        return response
    }

    suspend fun post(endpoint: String, jsonMap: JsonObject) : HttpResponse {
        val client = HttpClient(CIO);
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create(BACKEND_URL + endpoint))
//            .header("Content-Type", "application/json")
//            .POST(HttpRequest.BodyPublishers.ofString(jsonMap.toString()))
//            .build()
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        val response : HttpResponse = client.post(BACKEND_URL + endpoint) {
            contentType(ContentType.Application.Json)
            setBody(jsonMap.toString())
        }
        printRequestAndResponse("POST ==> ${BACKEND_URL + endpoint}", response, jsonMap.toString())
        return response
    }

    suspend fun put(endpoint: String, jsonMap: JsonObject) : HttpResponse {
        val client = HttpClient(CIO);
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create(BACKEND_URL + endpoint))
//            .header("Content-Type", "application/json")
//            .PUT(HttpRequest.BodyPublishers.ofString(jsonMap.toString()))
//            .build()
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        printRequestAndResponse(request, response, jsonMap.toString())
//        return response.body()

        val response : HttpResponse = client.put(BACKEND_URL + endpoint) {
            contentType(ContentType.Application.Json)
            setBody(jsonMap.toString())
        }
        printRequestAndResponse("POST ==> ${BACKEND_URL + endpoint}", response, jsonMap.toString())
        return response
    }

    suspend fun delete(endpoint: String, jsonMap: Map<String, String>) : HttpResponse {
        var deleteUrl = BACKEND_URL + endpoint + "?"
        for ((key, value) in jsonMap) {
            deleteUrl += key + "=" + value + "&"
        }
        val client = HttpClient(CIO);
//        val client = HttpClient.newBuilder().build()
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create(deleteUrl))
//            .DELETE()
//            .build()
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
//        printRequestAndResponse(request, response)

        val response : HttpResponse = client.delete(deleteUrl)
        printRequestAndResponse("DELETE ==> ${BACKEND_URL + endpoint}", response)
        return response
    }

}

