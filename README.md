# MyRxjavaRetrofitDemo
retrofit2  rxjava demo


### 一、添加各个依赖的包
 ```
    compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'

    compile 'com.squareup.okhttp3:okhttp:3.8.1'

    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
// Because RxAndroid releases are few and far between, it is recommended you also
// explicitly depend on RxJava's latest version for bug fixes and new features.
    compile 'io.reactivex.rxjava2:rxjava:2.1.0'

    compile group: 'com.google.code.gson', name: 'gson', version: '2.8.1'


    compile 'com.jakewharton:butterknife:8.7.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.7.0'
 ```

----
###库的由来
##### 1、 retrofit 的包

 项目 地址  [Retrofit](https://github.com/square/retrofit)

1. 导入 `compile 'com.squareup.retrofit2:retrofit:2.3.0'`

2. 在`~/retrofit-adapters` 中我们可以看到需要使用的执行默认回调的适配器。需要导入` ~/retrofit-adapters/rxjava2`中 看到的  
```
compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
```

3. 导入`~/retrofit/retrofit-converters`中说到的转换器 我们使用gson所以导入`~/retrofit/retrofit-converters/gson`下说的
```
compile 'com.squareup.retrofit2:converter-gson:2.3.0'
```

##### 2、 rxjava2 +rxandroid 的包

项目地址 [RxAndroid：Android](https://github.com/ReactiveX/RxAndroid)

导入需要的
```
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
// Because RxAndroid releases are few and far between, it is recommended you also
// explicitly depend on RxJava's latest version for bug fixes and new features.
compile 'io.reactivex.rxjava2:rxjava:2.1.0'
```

#### 3、 导入 gson 库
项目地址 [gson](http://www.mvnrepository.com/artifact/com.google.code.gson/gson/2.8.1)
```
compile group: 'com.google.code.gson', name: 'gson', version: '2.8.1'
```
#### 4、buttonknife库
项目地址[Butter Knife](http://jakewharton.github.io/butterknife/)
   导入
```
compile 'com.jakewharton:butterknife:8.7.0'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.7.0'
```

----

至于rxjava 和retrofit的一些基本知识建议学习作者是 **扔物线**的两篇文章 [给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083)和[RxJava 与 Retrofit 结合的最佳实践](http://gank.io/post/56e80c2c677659311bed9841)。
不过他的里面用到的都是1.x的库，在用到2.x的时候会有一些问题。主要是retrofit2中改变了一些东西。在retrofit的项目中的wiki   [What's different in 2.0](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)中官方有解释说改动了哪些地方。在其中的` Subscriber`部分中说更改了subscriber。这点要注意。
>Due to the name conflict, replacing the package from rx to org.reactivestreams is not enough. In addition, org.reactivestreams.Subscriber has no notion of adding resources to it, cancelling it or requesting from the outside.
>To bridge the gap we defined abstract classes DefaultSubscriber, ResourceSubscriber and DisposableSubscriber (plus their XObserver variants) for Flowable (and Observable) respectively that offers resource tracking support (of Disposables) just like rx.Subscriber and can be cancelled/disposed externally via dispose():

----

##开始使用
我们使用机房监控系统的登陆接口来测试。
#### 1、添加联网权限
```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
#### 2、定义返回的数据模型类
```

public class LoginResult {
    /**
     * success : 0
     * msg : User authorize success!
     * data : {"token":"eyJ1c2VybmFtZSI6ICJ4dXNodW4iLCAiZXhwIjogIjIwMTctMDctMjMgMDY6NTc6MTIifQ=="}
     */

    private int success;
    private String msg;
    private DataBean data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : eyJ1c2VybmFtZSI6ICJ4dXNodW4iLCAiZXhwIjogIjIwMTctMDctMjMgMDY6NTc6MTIifQ==
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}

```
#### 3、定义一个接口 
```
public interface LoginService {

    @FormUrlEncoded
    @POST("login")
    Observable<LoginResult> login(@Field("username") String username, @Field("password") String password);
}
```
注意使用@Field 注解的时候要用@FormUrlEncoded保证他的编码格式与URL的一致，官方有说道。
#### 4、定义一个类专门用来处理登陆的函数。

```
public class LoginMethod {
    public static final String BASE_LOGIN_URL = "http://*******/";
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
```

如果要自己更多去处理异常等信息。可以自己定义gsonconverter 和变换函数等来处理信息。

#### 5、使用上面的封装好的类。

 ```
        LoginMethod.getInstance().login(subscribe,"xushun","123456");
 ```

其中 subscribe 为；

```
private Observer<LoginResult> subscribe ;
        
        
        
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
```

在onNext中即可以获取到服务器返回来的信息。

大功告成。师父领进门，修行在个人。继续努力。

