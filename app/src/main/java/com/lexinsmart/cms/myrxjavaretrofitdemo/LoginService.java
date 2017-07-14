package com.lexinsmart.cms.myrxjavaretrofitdemo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xushun on 2017/7/14.
 */

public interface LoginService {

    @FormUrlEncoded
    @POST("login")
    Observable<LoginResult> login(@Field("username") String username, @Field("password") String password);
}
