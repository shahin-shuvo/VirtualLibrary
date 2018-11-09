package com.sasam.virtuallibrary.BookRequest;

public class BookRequestData {
    private String reqId;
    private  String reqBookName;
    private String reqUserId;
    private String reqUserName;
    private String reqBookId;
    public BookRequestData(){}
    public BookRequestData(String reqId, String reqBookName, String reqBookId ,String reqUserId,String reqUserName){
        this.reqId =reqId;
        this.reqBookName = reqBookName;
        this.reqBookId = reqBookId;
        this.reqUserName = reqUserName;
        this.reqUserId = reqUserId;

    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqBookName() {
        return reqBookName;
    }

    public void setReqBookName(String reqBookName) {
        this.reqBookName = reqBookName;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public String getReqBookId() {
        return reqBookId;
    }

    public void setReqBookId(String reqBookId) {
        this.reqBookId = reqBookId;
    }

}
