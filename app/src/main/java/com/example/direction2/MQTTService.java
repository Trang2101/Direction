package com.example.direction2;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class MQTTService extends MqttService {

    //        public static final String BROKER_URL = "tcp://192.168.50.21:1883";
    //public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
//    public static final String BROKER_URL = "tcp://192.168.0.152:1883";
    public static final String BROKER_URL = "tcp://192.168.123.21:1883";


    public static final String clientId = "ID";

    public static final String TOPIC = "tag0";
    private MqttClient mqttClient;


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent = new Intent(this, MapsActivity.class);
        Timer timer = new Timer();
        Intent finalIntent = intent;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Thực hiện tác vụ in ra màn hình
                try {
                    mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
                    // mqttClient.setCallback(new PushCallback(this));
                    Log.d("TAG", "onCreate: Hello");
                    mqttClient.connect();
                    Log.d("TAG", "onCreate: Hi");
                    //Subscribe to all subtopics of homeautomation
                    mqttClient.subscribe(TOPIC);
                    byte[] encodedPayload = "12.3;22.5".getBytes(StandardCharsets.UTF_8);
                    MqttMessage message = new MqttMessage(encodedPayload);
                    mqttClient.publish(TOPIC, message);

                    mqttClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            // Xử lý khi mất kết nối tới broker MQTT
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            // Xử lý khi nhận được tin nhắn MQTT
                            String payload = new String(message.getPayload());
                            Log.d("TAG", "Received message: " + payload);

//                            finalIntent.putExtra("data_key", payload);
                            sendMessageToActivity(MQTTService.this,payload);
//                            finalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(finalIntent);
//                            mqttClient.setCallback(null);
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                            // Xử lý khi tin nhắn đã được gửi thành công
                        }
                    });
//                    mqttClient.setCallback(mqttCallback);


                } catch (MqttException e) {
                    //Looper.prepare();
                    Log.d("TAG", "run: MqttException" + e.getLocalizedMessage());
                    //Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    //e.printStackTrace();
                    //Looper.loop();
                }
            }
        };

        timer.schedule(task, 000, 5000);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            Log.d("TAG", "onDestroy: destroy");
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            //Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("TAG", "run: onDestroy" + e.getLocalizedMessage());
            //e.printStackTrace();
        }
    }

    private static void sendMessageToActivity(Context context, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        intent.putExtra("Status", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

