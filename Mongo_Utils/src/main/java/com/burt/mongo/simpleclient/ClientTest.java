package com.burt.mongo.simpleclient;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class ClientTest {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> testCol = database.getCollection("test_col");
        Document document = new Document("name", "Udipi")
                .append("contact", new Document("phone", "001-444-0149")
                        .append("email", "udipi@example.com")
                        .append("location", Arrays.asList(-177.92502, 110.8279556)))
                .append("stars", 4)
                .append("categories", Arrays.asList("Food", "Restaurant", "Fast Food"));

        testCol.insertOne(document);
        System.out.println("END");
    }
}
