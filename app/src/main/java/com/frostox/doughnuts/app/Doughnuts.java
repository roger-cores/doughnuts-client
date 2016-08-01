package com.frostox.doughnuts.app;

import android.app.Application;

import com.frostox.doughnuts.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bloss on 1/8/16.
 */
public class Doughnuts extends Application {
    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();





    }

    public Retrofit getClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }


}
