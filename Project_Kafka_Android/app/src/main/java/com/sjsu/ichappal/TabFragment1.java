
package com.sjsu.ichappal;
//import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.lang.String;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sjsu.ichappal.helpers.StepDetector;
import com.sjsu.ichappal.helpers.StepListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TabFragment1 extends Fragment implements SensorEventListener, StepListener {

    private TextView textView, TvSteps;
    private Button start, stop;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private FirebaseAuth auth;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_tab1, container, false);
        TvSteps = (TextView) view.findViewById(R.id.tv_steps);
        final Button start = (Button) view.findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numSteps = 0;
                registerListener();
            }
        });

        final Button stop = (Button) view.findViewById(R.id.btn_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unregisterListener();
            }
        });

        return view;
    }

    public void registerListener() {
        if(sensorManager != null) {
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void unregisterListener() {
        if(sensorManager != null) {
            sensorManager.unregisterListener(this, accel);
        }

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        Log.d("Username",auth.getCurrentUser().getEmail());


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
          /*  Map<String, Object> accel_data = new HashMap<>();
            accel_data.put("X",  event.values[0]);
            accel_data.put("Y", event.values[1]);
            accel_data.put("Z", event.values[2]);
            accel_data.put("timestamp", event.timestamp);

            db.collection("accelerometerdata")
                    .add(accel_data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Added", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error adding document", e);
                        }
                    }); */
        }
    }

    @Override
    public void step(long timeNs) throws JSONException {
        numSteps++;
        new HTTPAsyncTask().execute("http://kafkapublisher-env.8tsy628pba.us-east-1.elasticbeanstalk.com/api/publish");
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private JSONObject buidJsonObject(int numSteps) throws JSONException {
        String timeStamp = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("userId", auth.getCurrentUser().getEmail());
        jsonObject.accumulate("dateTime", timeStamp );
        jsonObject.accumulate("step", numSteps );

        return jsonObject;
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject =  buidJsonObject(numSteps);

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }
    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                return HttpPost(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
      Log.d("post request"," post done");
    }
}

}