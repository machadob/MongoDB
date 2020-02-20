package com.burt.mongo.changestream;

import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ChangeStreamTest {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> testCol = db.getCollection("test_col");
        MongoCursor<ChangeStreamDocument<Document>> cursor = testCol.watch().iterator();
        ChangeStreamDocument<Document> next = null;
        while(true){
            if(cursor.hasNext())
            {
                next = cursor.next();
                if(next.getOperationType() == OperationType.INSERT || next.getOperationType() == OperationType.UPDATE){
                    if(next.getFullDocument() != null){
                        System.out.println("Doc is : " + next.getFullDocument().toJson());
                    }
                }
            }
        }
    }
}

// /usr/local/opt/mongodb-community/bin/mongod --dbpath /Users/shaun/0000_LEARN/MongoDB/data --logpath /Users/shaun/0000_LEARN/MongoDB/Logs/mlog_00.log  --replSet testrepl --noauth
