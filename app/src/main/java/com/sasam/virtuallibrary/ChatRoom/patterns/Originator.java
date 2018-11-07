package com.sasam.virtuallibrary.ChatRoom.patterns;

//Memento pattern
public class Originator {

    private String state;


    public void setState(String state) {
        this.state = state;
    }

    public ChatMemento save(){
        return new ChatMemento(state);
    }

    public String restore(ChatMemento m){
        state=m.getState();
        return state;
    }
}
