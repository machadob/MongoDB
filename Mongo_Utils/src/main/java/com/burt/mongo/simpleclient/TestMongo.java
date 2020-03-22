package com.burt.mongo.simpleclient;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class TestMongo {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        TestMongo testMongo = new TestMongo();
        testMongo.test_001(mongoClient);
        mongoClient.close();
        System.out.println("END");
    }

    private void addTestData(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("inventory");

        collection.insertMany(Arrays.asList(
                Document.parse("{ item: 'journal', qty: 25, size: { h: 14, w: 21, uom: 'cm' }, status: 'A' }"),
                Document.parse("{ item: 'notebook', qty: 50, size: { h: 8.5, w: 11, uom: 'in' }, status: 'A' }"),
                Document.parse("{ item: 'paper', qty: 100, size: { h: 8.5, w: 11, uom: 'in' }, status: 'D' }"),
                Document.parse("{ item: 'planner', qty: 75, size: { h: 22.85, w: 30, uom: 'cm' }, status: 'D' }"),
                Document.parse("{ item: 'postcard', qty: 45, size: { h: 10, w: 15.25, uom: 'cm' }, status: 'A' }")
        ));
        System.out.println("END");
    }

    private void test_001(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("inventory");
        FindIterable<Document> cursor = collection.find(or(eq("status", "A"), lt("qty", 30)));
        for (Document d : cursor) {
            System.out.println(d);
        }
    }

    private void test_002(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("test_col");
        BasicDBObject document = new BasicDBObject();
        DBObject query = QueryBuilder.start("num1").lessThanEquals(7).and("num2").greaterThanEquals(7).get();
//        document.putAll(query.get());
//        collection.find(query.get());
        BasicDBObject allQuery = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();
        fields.put("name", 1);

        FindIterable<Document> cursor = collection.find((ClientSession) allQuery, fields);
        for (Document d : cursor) {
            System.out.println(d);
        }
    }

    private void test_003(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("test_col");
        FindIterable<Document> cursor = collection.find();
        for (Document d : cursor) {
            System.out.println(d);
        }
        System.out.println("END");
    }

    private void test_004(MongoClient mongoClient) {
//        populateCollection(mongoClient, "test", "test_col");
        populateCollection(mongoClient, "test", "test_col");
//        populateCollectionMany(mongoClient, "test", "test_col");
        List<String> colList = getCollection(mongoClient, "test", "test_col");
        System.out.println("END");
    }

    private static void populateCollection(MongoClient mongoClient, String dataBaseStr, String collectionStr) {
        MongoDatabase database = mongoClient.getDatabase(dataBaseStr);
        MongoCollection<Document> testCol = database.getCollection(collectionStr);
        Document document = new Document("name", "Bdipi")
                .append("contact", new Document("phone", "001-444-0149")
                        .append("email", "Bdipi@example.com")
                        .append("location", Arrays.asList(-177.92502, 110.8279556)))
                .append("stars", 4)
                .append("categories", Arrays.asList("Food", "Restaurant", "Fast Food"));
        testCol.insertOne(document);

    }

    private static void populateCollection1(MongoClient mongoClient, String dataBaseStr, String collectionStr) {
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
        testCol.insertOne(document);
        document = new Document("name", "Sunny Joe")
                .append("contact", new Document("phone", "001-444-0144")
                        .append("email", "sunny.joe@example.com")
                        .append("location", Arrays.asList(-180.92502, 110.8279556)))
                .append("stars", 5)
                .append("food", Arrays.asList("Fish", "Beef", "Wine"));
        testCol.insertOne(document);
    }

    public static List<String> getCollection(MongoClient mc, String db, String col) {
        List<String> colList = new ArrayList<>();
        MongoDatabase mdb = mc.getDatabase(db);
        MongoCollection<Document> collection = mdb.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                colList.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        return colList;
    }

    public static List<String> getFilteredCollection(MongoClient mc, String db, String col) {
        List<String> colList = new ArrayList<>();
        MongoDatabase mdb = mc.getDatabase(db);
        MongoCollection<Document> collection = mdb.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                colList.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        return colList;
    }
}
