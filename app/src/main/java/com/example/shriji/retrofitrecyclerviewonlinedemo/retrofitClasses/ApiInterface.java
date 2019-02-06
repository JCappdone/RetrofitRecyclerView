package com.example.shriji.retrofitrecyclerviewonlinedemo.retrofitClasses;


import com.example.shriji.retrofitrecyclerviewonlinedemo.models.UsersListModel;
import com.example.shriji.retrofitrecyclerviewonlinedemo.utils.APIClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(APIClass.USERS_LIST)
    Call<UsersListModel> getUserList(@Query("page") String page, @Query("per_page") String per_page);

}
