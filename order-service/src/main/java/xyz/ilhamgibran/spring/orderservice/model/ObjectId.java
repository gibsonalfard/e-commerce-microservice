package xyz.ilhamgibran.spring.orderservice.model;

import java.sql.Timestamp;

public class ObjectId {
    private Timestamp timestamp;
    private String date;

    public ObjectId(){
        super();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
