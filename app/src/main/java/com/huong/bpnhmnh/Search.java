package com.huong.bpnhmnh;

public class Search {
    private String Name;
    private int Photo;

    public Search(){

    }

    public Search(String name, int photo){
        Name = name;
        Photo = photo;
    }

    public String getName(){
        return Name;
    }

    public int getPhoto(){
        return Photo;
    }

    public void setName(String name){
        Name = name;
    }

    public void setPhoto(int photo){
        Photo = photo;
    }

}
