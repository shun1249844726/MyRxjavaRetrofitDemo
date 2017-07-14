package com.lexinsmart.cms.myrxjavaretrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {
    private Observer<LoginResult> subscribe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscribe = new Observer<LoginResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull LoginResult loginResult) {
                System.out.println("tag:"+loginResult.getData().getToken());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        LoginMethod.getInstance().login(subscribe,"xushun","123456");

    }
}
