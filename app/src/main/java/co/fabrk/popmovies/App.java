package co.fabrk.popmovies;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Created by ebal on 05/04/16.
 */
public class App extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}