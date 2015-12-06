package com.roosevelt.thermostat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.roosevelt.thermostat.util.SystemUiHider;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Thermostat extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private RooseveltAPI api;
    private TextView inside;
    private TextView outside;
    private TextView setpoint;

    private volatile int setPoint;
    private Handler handler = new Handler();
    private Runnable poller = new Runnable() {
        @Override
        public void run() {
            Call<ThermostatStatus> status = api.status();
            status.enqueue(callback);
            handler.postDelayed(poller, 1000 * 10);
        }
    };

    private String formatTemp(double temp) {
        return String.format("%.1f", temp);
    }

    private String formatSetpoint(double setpoint) {
        return String.format("%.0f", setpoint);
    }

    public void colder(View button) {
        int newTemp = setPoint - 1;
        SetTempRequest req = new SetTempRequest(newTemp, 60);
        Call<ThermostatStatus> setTemp = api.setTemp(req);
        setTemp.enqueue(callback);
    }

    public void hotter(View button) {
        int newTemp = setPoint + 1;
        SetTempRequest req = new SetTempRequest(newTemp, 60);
        Call<ThermostatStatus> setTemp = api.setTemp(req);
        setTemp.enqueue(callback);
    }


    Callback<ThermostatStatus> callback = new Callback<ThermostatStatus>() {
        @Override
        public void onResponse(Response<ThermostatStatus> response, Retrofit retrofit) {
            if (response.isSuccess() && response.body() != null) {
                ThermostatStatus status = response.body();
                inside.setText(formatTemp(status.getInside()));
                outside.setText(formatTemp(status.getOutside()));
                setpoint.setText(formatSetpoint(status.getSetpoint()));
                setPoint = (int)(status.getSetpoint());
                if (status.isHeat_on()) {
                    setpoint.setBackgroundColor(Color.RED);
                } else {
                    setpoint.setBackgroundColor(Color.GRAY);
                }
            }

        }

        @Override
        public void onFailure(Throwable t) {
            outside.setText("Failure!");
            Log.e("Therm", "Failure!", t);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_thermostat);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                // 10.0.2.2 is the computer the emulator is running on
                .baseUrl("http://735roosevelt.com")
                .build();

        api = retrofit.create(RooseveltAPI.class);

        // Kick off the polling loop to update the UI
        handler.post(poller);
        setContentView(R.layout.activity_thermostat);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        inside = (TextView)findViewById(R.id.inside_temp);
        outside = (TextView)findViewById(R.id.outside_temp);
        setpoint = (TextView)findViewById(R.id.setpoint);
    }



}
