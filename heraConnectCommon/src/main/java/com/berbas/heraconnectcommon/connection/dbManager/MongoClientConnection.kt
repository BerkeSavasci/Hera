package com.berbas.heraconnectcommon.connection.dbManager

import org.bson.Document
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.firstOrNull

class MongoClientConnection {
    /*private val uri = "mongodb+srv://sgey:rohecE8fOyfg1yzW@cluster0.galdk78.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    private val mongoClient = MongoClient.create(uri)

    data class Movie(val title: String, val year: Int, val cast: List<String>)

    fun main() {
        // Replace the placeholders with your credentials and hostname
        val connectionString = uri
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        // Create a new client and connect to the server
        MongoClient.create(mongoClientSettings).use { mongoClient ->
            val database = mongoClient.getDatabase("admin")
            runBlocking {
                database.runCommand(Document("ping", 1))
            }
            println("Pinged your deployment. You successfully connected to MongoDB!")
        }
    }

    fun getMovie() {
        val database = mongoClient.getDatabase("sample_mflix")
        val collection = database.getCollection<Document>("movies")

        runBlocking {
            val doc = collection.find(eq("title", "Back to the Future")).firstOrNull()
            if (doc != null) {
                println(doc.toJson())
            } else {
                println("No matching documents found.")
            }
        }

        mongoClient.close()
    }
*/
}