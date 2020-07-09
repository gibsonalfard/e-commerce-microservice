package xyz.ilhamgibran.spring.orderservice.model;

import java.sql.Timestamp;

public class ObjectId {
    private Long timestamp;
    private String date;

    public ObjectId(){
        super();
    }

    public ObjectId(Long timestamp, String date){
        this.timestamp = timestamp;
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
