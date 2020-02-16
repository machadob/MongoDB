package com.burt.mongo.simpleclient;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientTest {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
//        populateCollection(mongoClient, "test", "test_col");
//        populateCollectionMany(mongoClient, "test", "test_col");
        List<String> colList = getCollection(mongoClient, "test", "test_col");
        mongoClient.close();
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

    private static void populateCollectionMany(MongoClient mongoClient, String dataBaseStr, String collectionStr) {
        MongoDatabase database = mongoClient.getDatabase(dataBaseStr);
        MongoCollection<Document> testCol = database.getCollection(collectionStr);
        Document document = new Document("name", "John Doe")
                .append("contact", new Document("phone", "001-444-0141")
                        .append("email", "john.doe@example.com")
                        .append("location", Arrays.asList(-177.92502, 110.8279556)))
                .append("stars", 4)
                .append("food", Arrays.asList("Burger", "Pizza", "Taco"));
        testCol.insertOne(document);
        document = new Document("name", "Jane Moe")
                .append("contact", new Document("phone", "001-444-0142")
                        .append("email", "jane.moe@example.com")
                        .append("location", Arrays.asList(-178.92502, 110.8279556)))
                .append("stars", 3)
                .append("food", Arrays.asList("Pasta", "Salad", "Coffee"));
        testCol.insertOne(document);
        document = new Document("name", "Sam Loe")
                .append("contact", new Document("phone", "001-444-0143")
                        .append("email", "sam.loe@example.com")
                        .append("location", Arrays.asList(-179.92502, 110.8279556)))
                .append("stars", 4)
                .append("food", Arrays.asList("Apple", "Dates", "Milk"));
        testCol.insertOne(document);        document = new Document("name", "Sunny Joe")
                .append("contact", new Document("phone", "001-444-0144")
                        .append("email", "sunny.joe@example.com")
                        .append("location", Arrays.asList(-180.92502, 110.8279556)))
                .append("stars", 5)
                .append("food", Arrays.asList("Fish", "Beef", "Wine"));
        testCol.insertOne(document);
    }

    public static List<String> getCollection(MongoClient mc, String db, String col){
        List<String> colList = new ArrayList<>();
        MongoDatabase mdb = mc.getDatabase(db);
        MongoCollection<Document> collection = mdb.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        try{
            while(cursor.hasNext()){
                colList.add(cursor.next().toJson());
            }
        }finally {
            cursor.close();
        }
        return colList;
    }

    public static List<String> getFilteredCollection(MongoClient mc, String db, String col){
        List<String> colList = new ArrayList<>();
        MongoDatabase mdb = mc.getDatabase(db);
        MongoCollection<Document> collection = mdb.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        try{
            while(cursor.hasNext()){
                colList.add(cursor.next().toJson());
            }
        }finally {
            cursor.close();
        }
        return colList;
    }
}
