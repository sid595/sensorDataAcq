package com.acad_example.nilea.sensordataacq;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class getSensorsData extends AppCompatActivity implements SensorEventListener{
    public static SensorManager sensorManager ;
    public static Sensor gyroSensor,accSensor,mgnSensor;
    public static final double BUFFER_LENGTH = 32, HALT_PROCESS = 4;
    //Since Async processes will be running, it is important to keep track of which sensor processing
    //is going on. Although we will require simultaneous processing, it is better to keep track of
    //indivisual process

    public static boolean canProcess, accProcessRunning = false, mgnProcessRunning = false,
                gyroProcessRunning=false;

    double xGyroData=0,yGyroData=0,zGyroData=0,xAccData=0,yAccData=0,zAccData=0,xMgnData=0,yMgnData=0,zMgnData=0;
    int bGyroCount = 0,bAccCount = 0,bMgnCount = 0;
    public static TextView xGyroDataDisp, yGyroDataDisp,zGyroDataDisp,xAccDataDisp, yAccDataDisp,
            zAccDataDisp,xMgnDataDisp, yMgnDataDisp,zMgnDataDisp;
    public static ArrayList bufferGyroX, bufferGyroY, bufferGyroZ,bufferAccX,bufferAccY, bufferAccZ,
            bufferMgnX,bufferMgnY, bufferMgnZ;

    //Trying to Implement 9 axis Gyroscope with analyzing 32 sensor pieces at a time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sensors_data);
        xGyroDataDisp = (TextView) findViewById(R.id.textView);
        yGyroDataDisp = (TextView) findViewById(R.id.textView2);
        zGyroDataDisp = (TextView) findViewById(R.id.textView5);
        xAccDataDisp = (TextView) findViewById(R.id.textView7);
        yAccDataDisp = (TextView) findViewById(R.id.textView6);
        zAccDataDisp = (TextView) findViewById(R.id.textView3);
        xMgnDataDisp = (TextView) findViewById(R.id.textView9);
        yMgnDataDisp = (TextView) findViewById(R.id.textView8);
        zMgnDataDisp = (TextView) findViewById(R.id.textView10);


        //haltProcess = (int) HALT_PROCESS;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mgnSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);

        sensorManager.registerListener(this,gyroSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,mgnSensor,SensorManager.SENSOR_DELAY_NORMAL);

        bufferAccX = new ArrayList();
        bufferAccY = new ArrayList();
        bufferAccZ = new ArrayList();
        bufferGyroX = new ArrayList();
        bufferGyroY = new ArrayList();
        bufferGyroZ = new ArrayList();
        bufferMgnX = new ArrayList();
        bufferMgnY = new ArrayList();
        bufferMgnZ = new ArrayList();


        for(int i =0; i< BUFFER_LENGTH;i++){
            bufferAccX.add(0);
            bufferAccY.add(0);
            bufferAccZ.add(0);
            bufferGyroX.add(0);
            bufferGyroY.add(0);
            bufferGyroZ.add(0);
            bufferMgnX.add(0);
            bufferMgnY.add(0);
            bufferMgnZ.add(0);
        }
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mgnSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Something with Sensors has occued.
        //Let's try to understand it
        setValues sV;
        int sensor = sensorEvent.sensor.getType();
        switch(sensor){
            case Sensor.TYPE_ACCELEROMETER:
                xAccData = sensorEvent.values[0];
                yAccData = sensorEvent.values[1];
                zAccData = sensorEvent.values[2];
                sV = new setValues(xAccData,yAccData,zAccData,1);
                sV.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                bufferAccX.remove(0);
                bufferAccY.remove(0);
                bufferAccZ.remove(0);
                bufferAccX.add(xAccData);
                bufferAccY.add(yAccData);
                bufferAccZ.add(zAccData);
                //haltProcess--;
                //if(haltProcess==0){
                //Do the processing
                //processBuffer proces = new processBuffer(bufferX,bufferY,bufferZ);
                //haltProcess = (int)HALT_PROCESS;
                //}

                break;
            case Sensor.TYPE_GYROSCOPE:
                xGyroData = sensorEvent.values[0];
                yGyroData = sensorEvent.values[1];
                zGyroData = sensorEvent.values[2];
                sV = new setValues(xGyroData,yGyroData,zGyroData,2);
                sV.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                bufferGyroX.remove(0);
                bufferGyroY.remove(0);
                bufferGyroZ.remove(0);
                bufferGyroX.add(xGyroData);
                bufferGyroY.add(yGyroData);
                bufferGyroZ.add(zGyroData);
                //haltProcess--;
                //if(haltProcess==0){
                //Do the processing
                //processBuffer proces = new processBuffer(bufferX,bufferY,bufferZ);
                //haltProcess = (int)HALT_PROCESS;
                //}
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                xMgnData = sensorEvent.values[0];
                yMgnData = sensorEvent.values[1];
                zMgnData = sensorEvent.values[2];
                sV = new setValues(xMgnData,yMgnData,zMgnData,3);
                sV.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                bufferMgnX.remove(0);
                bufferMgnY.remove(0);
                bufferMgnZ.remove(0);
                bufferMgnX.add(xMgnData);
                bufferMgnY.add(yMgnData);
                bufferMgnZ.add(zMgnData);
                //haltProcess--;
                //if(haltProcess==0){
                //Do the processing
                //processBuffer proces = new processBuffer(bufferX,bufferY,bufferZ);
                //haltProcess = (int)HALT_PROCESS;
                //}
                break;
        }
        //sV.execute();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class setValues extends AsyncTask<Void,Void,Void>{

        double xAxis,yAxis,zAxis;
        int sensorType;
        public setValues(double xAxisData, double yAxisData, double zAxisData, int sType) {
            xAxis=xAxisData; yAxis=yAxisData; zAxis=zAxisData;
            sensorType = sType;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            switch (sensorType){
                case 1:
                    xAccDataDisp.setText("X axis Gyroscope: " + xAxis);
                    yAccDataDisp.setText("Y axis Gyroscope: " + yAxis);
                    zAccDataDisp.setText("Z axis Gyroscope: " + zAxis);
                    break;
                case 2:
                    xGyroDataDisp.setText("X axis Accelerometer: " + xAxis);
                    yGyroDataDisp.setText("Y axis Accelerometer: " + yAxis);
                    zGyroDataDisp.setText("Z axis Accelerometer: " + zAxis);
                    break;
                case 3:
                    xMgnDataDisp.setText("X axis Magnetometer: " + xAxis);
                    yMgnDataDisp.setText("Y axis Magnetometer: " + yAxis);
                    zMgnDataDisp.setText("Z axis Magnetometer: " + zAxis);
                    break;

            }
        }
    }

    public static class processBuffer extends AsyncTask<Void,Void,Void>{

        ArrayList xData,yData,zData;
        int activityIndex=0;

        public processBuffer(ArrayList bufferX, ArrayList bufferY, ArrayList bufferZ, int aIndex) {
            xData = bufferX; yData = bufferY; zData = bufferZ; activityIndex = aIndex;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
