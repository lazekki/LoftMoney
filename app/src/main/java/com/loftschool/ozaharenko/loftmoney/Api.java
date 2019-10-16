package com.loftschool.ozaharenko.loftmoney;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type, @Query("auth-token") String token);

    @POST("items/add")
    Call<Status> addItem(@Body AddItemRequest request, @Query("auth-token") String token);

    @GET("auth")
    Call<Status> auth(@Query("social_user_id") String userId);

}
