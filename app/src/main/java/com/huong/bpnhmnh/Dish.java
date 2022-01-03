package com.huong.bpnhmnh;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable {
    private String name;
    private String image;
    private long time;
    private String recipe;
    private String introduce;
    private String author;


    public Dish() {
    }

    public Dish(String name, String image, long time, String recipe, String introduce, String author) {
        this.name = name;
        this.image = image;
        this.time = time;
        this.recipe = recipe;
        this.introduce = introduce;
        this.author = author;
    }

    protected Dish(Parcel in) {
        name = in.readString();
        image = in.readString();
        time = in.readLong();
        recipe = in.readString();
        introduce = in.readString();
        author = in.readString();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeLong(time);
        parcel.writeString(recipe);
        parcel.writeString(introduce);
        parcel.writeString(author);
    }
}
