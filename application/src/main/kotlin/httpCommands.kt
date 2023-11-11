import kotlinx.serialization.json.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class MyHttp {

    fun get(endpoint: String) : String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://0.0.0.0:8080/" + endpoint))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun post(endpoint: String, jsonMap: JsonObject) : String {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://0.0.0.0:8080/" + endpoint))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonMap.toString()))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body()
    }

    fun put(endpoint: String, jsonMap: JsonObject) : String {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://0.0.0.0:8080/" + endpoint))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jsonMap.toString()))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body()
    }

    fun delete(endpoint: String, jsonMap: Map<String, String>) : String {
        var deleteUrl = "http://0.0.0.0:8080/" + endpoint + "?"
        for ((key, value) in jsonMap) {
            deleteUrl += key + "=" + value + "&"
        }
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(deleteUrl))
            .DELETE()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

}

