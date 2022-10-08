package com.example.examenandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SingleUser {
    @SerializedName("data")
    public User data = new User();

    @SerializedName("support")
    public Support support = new Support();

    public class User{
        @SerializedName("id")
        public Integer id;
        @SerializedName("email")
        public String email;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("avatar")
        public String avatar;
    }

    public class Support{
        @SerializedName("url")
        public String url;
        @SerializedName("text")
        public String text;
    }
}
