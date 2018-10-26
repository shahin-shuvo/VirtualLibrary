package com.sasam.virtuallibrary.CodeGenerator;

import java.util.List;

public class NameRepository implements Container {
    public String names[] = {"Robert" , "John" ,"Julie" , "Lora"};
    public String code;
    List<String> list;
    @Override
    public Iterator getIterator() {
        return new NameIterator();
    }

    public NameRepository(List<String> list, String code)
    {
        this.list = list;
        this.code = code;
    }

    private class NameIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {

            if(index < list.size()){
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                String st = (String )list.get(index);
                if(st.equals(code))
                {
                  return "exist";
                }
                index++;
                return "no";
            }
            return null;
        }
    }
}

