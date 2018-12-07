package com.sjsu.ichappal.helpers;


import org.json.JSONException;

public interface StepListener {

    public void step(long timeNs) throws JSONException;

}