package com.unihackchallenge.mmtrafficreport.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.TimeUnit;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static String URL = APIConfig.TEST_BASE_URL;
    private static RetrofitClient mInstance;
    private RetrofitService mService;

    private RetrofitClient() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient httpClient;


        httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .build();


        //this code is to work both Realm and Gson


            Gson gson = new GsonBuilder().create();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        mService = retrofit.create(RetrofitService.class);


    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public RetrofitService getService() {
        return mService;
    }

}