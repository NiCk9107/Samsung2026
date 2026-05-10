package com.example.opharma.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ApiService {
    private val baseUrl = "http://10.0.2.2:8080"

    suspend fun login(login: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        request(
            url = "$baseUrl/login",
            method = "POST",
            body = """{"login":"$login","password":"$password"}"""
        )
    }

    suspend fun register(login: String, email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        request(
            url = "$baseUrl/register",
            method = "POST",
            body = """{"login":"$login","email":"$email","password":"$password"}"""
        )
    }

    suspend fun getMedicines(): Result<String> = withContext(Dispatchers.IO) {
        request(
            url = "$baseUrl/medicines",
            method = "GET"
        )
    }

    suspend fun getMedicineById(id: Int): Result<String> = withContext(Dispatchers.IO) {
        request(
            url = "$baseUrl/medicines/$id",
            method = "GET"
        )
    }

    suspend fun getCompatibility(medicineId: Int): Result<String> = withContext(Dispatchers.IO) {
        request(
            url = "$baseUrl/compatibility/$medicineId",
            method = "GET"
        )
    }

    private fun request(url: String, method: String, body: String? = null): Result<String> {
        val connection = (URL(url).openConnection() as HttpURLConnection)
        return try {
            connection.requestMethod = method
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.setRequestProperty("Accept", "application/json")

            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                connection.outputStream.use { os ->
                    os.write(body.toByteArray(Charsets.UTF_8))
                }
            }

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val response = stream?.readText().orEmpty()

            if (code in 200..299) {
                Result.success(response)
            } else {
                Result.failure(RuntimeException("HTTP $code: $response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            connection.disconnect()
        }
    }

    private fun InputStream.readText(): String =
        bufferedReader().use(BufferedReader::readText)
}