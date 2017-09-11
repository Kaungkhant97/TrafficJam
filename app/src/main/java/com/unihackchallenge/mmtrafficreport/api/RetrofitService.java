package com.unihackchallenge.mmtrafficreport.api;






import com.unihackchallenge.mmtrafficreport.LocationResponse;
import com.unihackchallenge.mmtrafficreport.location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by hhtet on 3/17/16.
 */
public interface RetrofitService {





    @POST(APIConfig.TRAFFIC_LIST)
    Call<location> postLocation(@Body location e);

    @GET(APIConfig.RECENT_TRAFFIC_LIST+"/{hr}")
    Call<LocationResponse> getRecentTraffic(@Path("hr") String hr);



}
