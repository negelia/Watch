package com.example.watch;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watch.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ImageView img;
    private SensorManager sysmanager;
    private Sensor sensor;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextView = binding.txt;
        img = binding.img;

        sysmanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sysmanager != null){
            sensor = sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
    }
    SensorEventListener sv = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(android.hardware.SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                float [] rotationMatrix = new float[16];
                float [] remappedRotationMatrix = new float[16];
                float[] orientations = new float[3];

                //запись изменений в обработчик событий
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values); //перезапись значений при повороте экрана
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix);
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for (int i = 0; i < 3; i++){
                    //orientations[i] = (float)(Math.toDegrees(orientations[i]));
                    orientations[0] = (float) Math.toDegrees(orientations[0]);
                    orientations[1] = (float) Math.toDegrees(orientations[1]);
                    orientations[2] = (float) Math.toDegrees(orientations[2]);

                }
                mTextView.setText(" Z: "+ orientations[0] +"\n Y: "+ orientations[1]+"\n X: "+ orientations[2]);
                //txt.setText(String.valueOf((int)orientations[2]));
                img.setRotation(-orientations[2]);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sysmanager.registerListener(sv,sensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sysmanager.unregisterListener(sv);
    }
}