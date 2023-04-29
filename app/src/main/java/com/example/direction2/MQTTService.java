package com.example.direction2;

import android.content.Context;
import android.content.Intent;
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

import java.util.Timer;

public class MQTTService extends MqttService {
    public static final String BROKER_URL = "tcp://192.168.1.9:1883";
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

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
            mqttClient.connect();
            mqttClient.subscribe(TOPIC);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Xử lý khi nhận được tin nhắn MQTT
                    String payload = new String(message.getPayload());
                    Log.d("TAG", "Received message: " + payload);

                    sendMessageToActivity(MQTTService.this, payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
//            mqttClient.connect();

        } catch (MqttException e) {
            Log.d("TAG", "run: MqttException" + e.getLocalizedMessage());
        }


        try {
            mqttClient.subscribe(TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            Log.d("TAG", "onDestroy: destroy");
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Log.d("TAG", "run: onDestroy" + e.getLocalizedMessage());
        }
    }

    private static void sendMessageToActivity(Context context, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        intent.putExtra("Status", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

