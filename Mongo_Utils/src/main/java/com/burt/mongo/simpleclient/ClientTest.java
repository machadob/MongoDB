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
        populateCollection(mongoClient, "test", "test_col");
        List<String> colList = getCollection(mongoClient, "test", "test_col");
        System.out.println("END");
    }

    private static void populateCollection(MongoClient mongoClient, String dataBaseStr, String collectionStr) {
        MongoDatabase database = mongoClient.getDatabase(dataBaseStr);
        MongoCollection<Document> testCol = database.getCollection(collectionStr);
        Document document = new Document("name", "Udipi")
                .append("contact", new Document("phone", "001-444-0149")
                        .append("email", "udipi@example.com")
                        .append("location", Arrays.asList(-177.92502, 110.8279556)))
                .append("stars", 4)
                .append("categories", Arrays.asList("Food", "Restaurant", "Fast Food"));
        testCol.insertOne(document);

    }

    public static List<String> getCollection(MongoClient mc, String db, String col){
        List<String> colList = new ArrayList<>();
        MongoDatabase mdb = mc.getDatabase(db);
        MongoCollection<Document> collection = mdb.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        try{
            colList.add(cursor.next().toJson());
        }finally {
            cursor.close();
        }
        return colList;
    }
}
