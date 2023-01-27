package com.example.scansens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager; //менеджер сенсоров
    private float[] rotationMatrix; //матрица поворота
    private float[] accelerometer; //данные с акселерометра
    private float[] geomagnetism; //данные геомагнитного датчика
    private float[] orientation; //матрица положения в пространстве
    //текстовые поля для вывода информации
    private float[] li;
    private TextView xyAngle;
    private TextView xzAngle;
    TextView zyAngle;

    TextView light;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//5.
        //получаем объект менеджера датчиков
        light = (TextView) findViewById(R.id.light);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        rotationMatrix = new float[16];
        accelerometer = new float[3];
        geomagnetism = new float[3];
        orientation = new float[3];
        // поля для вывода показаний
        xyAngle = (TextView) findViewById(R.id.xyValue);
        xzAngle= (TextView) findViewById(R.id.xzValue);
        zyAngle = (TextView) findViewById(R.id.zyValue);


    }

    @Override //6.
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void loadSensorData(SensorEvent event){
        final int type = event.sensor.getType();

        if(type == Sensor.TYPE_ACCELEROMETER){
            accelerometer = event.values.clone();
        }
        if(type == Sensor.TYPE_MAGNETIC_FIELD){
            geomagnetism = event.values.clone();
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText(""+event.values[0]);
        }

        loadSensorData(event);
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometer,geomagnetism);
        SensorManager.getOrientation(rotationMatrix,orientation);

        if((xyAngle == null) || (xzAngle == null) || (zyAngle == null)){
            xyAngle = (TextView) findViewById(R.id.xyValue);
            xzAngle = (TextView) findViewById(R.id.xzValue);
            zyAngle = (TextView) findViewById(R.id.zyValue);
        }


        xyAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[0]))));
        xzAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[1]))));
        zyAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[2]))));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}