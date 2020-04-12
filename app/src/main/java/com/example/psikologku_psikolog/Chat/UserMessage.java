package com.example.psikologku_psikolog.Chat;

public class UserMessage {

    private String curentUser;
    private String message;
    private String sended;
    private String message_type;
    private long createdAt;
    public UserMessage(){

    }
    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
    public UserMessage(String curentUser,String message, String sender, long createdAt, String type) {
        this.message = message;
        this.sended = sender;
        this.createdAt = createdAt;
        this.curentUser = curentUser;
        this.message_type = type;
    }

    public String getCurentUser() {
        return curentUser;
    }

    public void setCurentUser(String curentUser) {
        this.curentUser = curentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSended() {
        return sended;
    }

    public void setSended(String sender) {
        this.sended = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
