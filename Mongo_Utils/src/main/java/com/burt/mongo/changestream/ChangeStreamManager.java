package com.burt.mongo.changestream;

import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ChangeStreamManager {
    BlockingQueue<CacheValue> myqueue = new LinkedBlockingQueue();
    ExecutorService es = Executors.newCachedThreadPool();
    MongoClient mongoClient = null;
    MongoDatabase db = null;

    public ChangeStreamManager() {
        mongoClient = MongoClients.create();
        db = mongoClient.getDatabase("test");
    }

    String colls[] = new String[]{"abc","klm","xyz","pqr"};



    public void init(){
        List<String> list1 = Arrays.asList(colls);
        for(String col:list1){
            CacheListener c = new CacheListener(col);
            es.submit(c);
        }
    }

    class CacheListener implements Runnable {
        private String collectionName = null;

        public CacheListener(String collectionName) {
            this.collectionName = collectionName;
        }

        @Override
        public void run() {
//            MongoCollection<Document> testCol = db.getCollection("test_col");
            MongoCollection<Document> testCol = db.getCollection(collectionName);
            MongoCursor<ChangeStreamDocument<Document>> cursor = testCol.watch().iterator();
            ChangeStreamDocument<Document> next = null;
            while (true) {
                if (cursor.hasNext()) {
                    next = cursor.next();
                    if (next.getOperationType() == OperationType.INSERT) {
                        if (next.getFullDocument() != null) {
                            String json = next.getFullDocument().toJson();
                            System.out.println("Doc is : " + json);
                            myqueue.add(new CacheValue(ActionType.INSERT, json));
                        }
                    } else if (next.getOperationType() == OperationType.UPDATE) {
                        if (next.getFullDocument() != null) {
                            String json = next.getFullDocument().toJson();
                            System.out.println("Doc is : " + json);
                            myqueue.add(new CacheValue(ActionType.UPDATE, json));
                        }
                    }
                }
            }
        }
    }

    class CacheValue {
        private ActionType actionType;
        private String value;

        public CacheValue(ActionType actionType, String value) {
            this.actionType = actionType;
            this.value = value;
        }

        public ActionType getActionType() {
            return actionType;
        }

        public void setActionType(ActionType actionType) {
            this.actionType = actionType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    enum ActionType {
        INSERT,
        UPDATE,
        DELETE
    }
}
