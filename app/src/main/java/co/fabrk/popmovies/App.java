package co.fabrk.popmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;
import okhttp3.OkHttpClient;
import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * Created by ebal on 05/04/16.
 */
public class App extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
    }
}