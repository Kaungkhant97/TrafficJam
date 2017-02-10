package com.unihackchallenge.mmtrafficreport.api;






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

 /* @GET(APIConfig.EVENT_LIST)
  Call<EventResponse> getEventList();

  @GET(APIConfig.SUBJECT_LIST)
  Call<mSubjectResponse> getSubjectList();


  @GET(APIConfig.SUBJECT_LIST+"{major}/{year}/{mclass}")
  Call<mSubjectResponse> getSubjectList(@Path("major") String major,
                                        @Path("year") String year,
                                        @Path("mclass") String mclass);

  @GET(APIConfig.SUBJECT_LIST+"{major}/{year}/{mclass}/{day}")
  Call<mSubjectResponse> getSubjectList(@Path("major") String major,
                                        @Path("year") String year,
                                        @Path("mclass") String mclass,
                                        @Path("day") String day);

  @POST(APIConfig.EVENT_LIST)
  Call<Event> postEvent(@Body Event e);

  @POST(APIConfig.EVENT_LIST)
  Call<Event> getEventbetween(@Query("startDate") String startDate, @Query("endDate") String endDate);*/







}
