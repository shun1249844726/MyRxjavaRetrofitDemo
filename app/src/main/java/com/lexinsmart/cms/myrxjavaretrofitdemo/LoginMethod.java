package com.lexinsmart.cms.myrxjavaretrofitdemo;



import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xushun on 2017/7/14.
 */

public class LoginMethod {
    public static final String BASE_LOGIN_URL = "http://erm.lexinsmart.com/";
    private  static final  int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private  LoginService  mLoginService;

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final LoginMethod INSTANCE = new LoginMethod();
    }

    //获取单例
    public static LoginMethod getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private LoginMethod(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_LOGIN_URL)
                .build();

        mLoginService  = mRetrofit.create(LoginService.class);

    }
    public void login(Observer<LoginResult>subscriber, String uname, String psd){
        Observable observable = mLoginService.login(uname,psd);

        toSubscribe(observable,subscriber);
    }
    private <T> void toSubscribe(Observable<T> o, Observer<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Observer<? super T>) s);
    }
}
