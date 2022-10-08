package com.example.examenandroid.network;

import com.example.examenandroid.model.Register;
import com.example.examenandroid.model.ReqresCredentials;
import com.example.examenandroid.model.Login;
import com.example.examenandroid.model.SingleUser;
import com.example.examenandroid.model.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiReqres {
    @POST("login")
    Call<Login> getLogin(@Body ReqresCredentials c);

    @POST("register")
    Call<Register> getRegister(@Body ReqresCredentials c);

    @POST("logout")
    Call<Login> logout();

    @GET("users?")
    Call<UserList> getUsers(@Query("page") String page);

    @GET
    Call<SingleUser> getUser(@Url String url);
}
