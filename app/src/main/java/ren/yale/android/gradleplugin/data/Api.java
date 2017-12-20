package ren.yale.android.gradleplugin.data;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yale on 2017/12/20.
 */

public interface Api {
    @GET("Android/10/3")
    Observable<GankAndroid> getGankAndroid(@Query("aa") String aa);
}
