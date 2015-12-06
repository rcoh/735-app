package com.roosevelt.thermostat;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by russell on 11/24/15.
 */

public interface RooseveltAPI {
    @GET("api/thermostat/status")
    Call<ThermostatStatus> status();

    @POST("api/thermostat/set")
    Call<ThermostatStatus> setTemp(@Body SetTempRequest setTempRequest);
}


