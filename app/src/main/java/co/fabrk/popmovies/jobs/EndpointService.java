package co.fabrk.popmovies.jobs;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ebal on 05/04/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class EndpointService {

    private EndPointInterface mService;
    String BASE_URL = "https://www.googleapis.com/";


    @AfterInject
    void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(EndPointInterface.class);

    }

    public EndPointInterface getService() {
        return mService;
    }

}
