package com.sasam.virtuallibrary.ChatRoom.patterns;
//memento pattern
public class ChatMemento {

    private String state;

    public ChatMemento(String state){
        this.state=state;
    }

    public String getState() {
        return state;
    }
}
