package com.roosevelt.thermostat;

/**
 * Created by russell on 11/28/15.
 */
public class SetTempRequest {
    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTime_minutes() {
        return time_minutes;
    }

    public void setTime_minutes(int time_minutes) {
        this.time_minutes = time_minutes;
    }

    int temp;
    int time_minutes;
    public SetTempRequest(int temp, int time_minutes) {
       this.temp = temp;
       this.time_minutes = time_minutes;
    }
}
