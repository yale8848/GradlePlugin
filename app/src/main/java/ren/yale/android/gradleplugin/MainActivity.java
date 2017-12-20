package ren.yale.android.gradleplugin;

import android.app.Activity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import ren.yale.android.gradleplugin.data.Api;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class MainActivity extends Activity {

    Retrofit mRetrofit;
    Api mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .client(getOkHttpClient())
               // .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApi =  mRetrofit.create(Api.class);
        mApi.getGankAndroid("bb");

    }

    public OkHttpClient getOkHttpClient(){
        okhttp3.OkHttpClient.Builder clientBuilder=new okhttp3.OkHttpClient.Builder();
        clientBuilder.readTimeout(20, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(20, TimeUnit.SECONDS);
        return clientBuilder.build();
    }
}
