package com.sasam.virtuallibrary.ChatRoom.patterns;

//memento pattern

import java.util.ArrayList;
import java.util.List;

public class CareTaker {

    private List<ChatMemento> mementos=new ArrayList<>();
    private int index;

    public void addMemento(ChatMemento memento){
        mementos.add(memento);
        index=mementos.size()-1;
        if(mementos.size()>100){
            mementos.remove(0);
            index=mementos.size()-1;
        }
    }

    public List<ChatMemento> getMementos() {
        return mementos;
    }

    public ChatMemento moveBackward(){
        if(index>=0){
            index--;
            return mementos.get(index+1);
        }
        else {
            index=-1;
            return new ChatMemento("");
        }
    }

    public ChatMemento moveForward(){
        int size=mementos.size();
        if(index<(size-1)){
            index++;
            return mementos.get(index);
        }
        else if(size==0)
            return new ChatMemento("");
        else
            return mementos.get(size-1);
    }
}
