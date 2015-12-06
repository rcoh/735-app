package com.roosevelt.thermostat;

/**
 * Created by russell on 11/24/15.
 */
public class ThermostatStatus {
    private boolean heat_on;
    private double inside;
    private double outside;
    private double setpoint;

    public boolean isHeat_on() {
        return heat_on;
    }

    public void setHeat_on(boolean heat_on) {
        this.heat_on = heat_on;
    }

    public double getInside() {
        return inside;
    }

    public void setInside(double inside) {
        this.inside = inside;
    }

    public double getOutside() {
        return outside;
    }

    public void setOutside(double outside) {
        this.outside = outside;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }
}

/*
{
  "heat_on": false,
  "inside": 62.375,
  "outside": 56.0,
  "setpoint": 50
}
 */
