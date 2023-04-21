package com.example.direction2;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.direction2.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String SERVICE_CLASSNAME = "com.example.myapplication.MQTTService";
    ActivityMapsBinding binding;
    private GoogleMap mMap;
    private double lat0 = 21.0380955;
    private double long0 = 105.78323;
    private Boolean state = true;
    private Boolean stateDirection = true;
    private List<Vert> listVert = ListVert.getInstance().getList();
    private String[] sourceString = ListVert.getInstance().getString();
    private String location;

    Vert vStart = listVert.get(0);
    Vert vEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
//        List<Vert> listVert = new ArrayList<Vert>();

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceString);

        ArrayAdapter<String> spinnerItemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceString);
        binding.autoComplete.setAdapter(itemsAdapter);
        binding.spinnerStart.setAdapter(spinnerItemsAdapter);
        binding.spinnerEnd.setAdapter(spinnerItemsAdapter);
        //

        startBlackIceService();
        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                location = intent.getStringExtra("Status");

                System.out.println("data: " + location);
                double x = 0, y = 0;
                if (location != null) {
                    String[] splits = location.split(";");
                    x = Double.parseDouble(splits[0]);
                    y = Double.parseDouble(splits[1]);
                    Log.d("TAG", "x , y: " + x + " " + y);
                }
                Vert v = new Vert();
                v = convertToGlbLat(x, y);
                if (location != null) {
                    Log.d("TAG", "glb x , y: " + v.getLat() + " " + v.getLng());
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(v.getLat(), v.getLng())));
                    LatLng tempL = new LatLng(v.getLat(), v.getLng());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempL, 26));
                }
            }
        };
        LocalBroadcastManager.getInstance(MapsActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


//        LatLngBounds newarkBounds = new LatLngBounds(
//                new LatLng(21.037985, 105.783142),       // tây nam corner - điểm cuối bên trái
//                new LatLng(21.038186, 105.783621));      // đông bắc corner - điểm trên bên phải
        LatLng g2 = new LatLng(21.0380824, 105.783381); // tạo location có tọa độ lat, long tương ứng g2
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(g2, 20));// move camera tới toa do g2.
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.tang1g2))
                .bearing(1.5f)
                .position(g2, 49.3883f, 24.8f);
        mMap.addGroundOverlay(newarkMap);

        System.out.println("data map ready: " + location);

//       String  yourData = intent.getStringExtra("Status");


//        for (int i = 1; i < listVert.size(); i++) {
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(listVert.get(i).getLat(), listVert.get(i).getLng())));
//        }

        binding.autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                for (int i = 0; i < 16; i++) {
                    mMap.clear();
                    if (binding.autoComplete.getText().toString().equals(listVert.get(i).getName())) {
                        vEnd = listVert.get(i);
                        mMap.addGroundOverlay(newarkMap);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(vEnd.getLat(), vEnd.getLng())));
                        LatLng tempL = new LatLng(vEnd.getLat(), vEnd.getLng());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempL, 26));
                        break;
                    }
                }
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                binding.dicrectionButton.setVisibility(View.VISIBLE);
            }
        });
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(v14.getLat(), v14.getLng())));

        //xử lý nút điều hướng

        binding.dicrectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dicrectionButton.setVisibility(View.GONE);
                findShortestPath(vStart, vEnd);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(g2, 20));
                binding.cancelButton.setVisibility(View.VISIBLE);
//                binding.dicrectionButton.setVisibility(View.GONE);
//                findShortestPath(vStart, vEnd);
//                binding.cancelButton.setVisibility(View.VISIBLE);
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng g2 = new LatLng(21.0380824, 105.783381); // tạo location có tọa độ lat, long tương ứng g2
                GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.tang1g2))
                        .bearing(1.5f)
                        .position(g2, 49.3883f, 24.8f);
                mMap.addGroundOverlay(newarkMap);
                binding.dicrectionButton.setVisibility(View.GONE);
                binding.cancelButton.setVisibility(View.GONE);
                if (!state)
                    binding.dicrectionButton.setVisibility(View.VISIBLE);
            }
        });

        // chuyển từ tìm phòng sang tìm đường giữa phòng/phòng

        binding.swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {
                    binding.autoComplete.setVisibility(View.INVISIBLE);
                    binding.subContainer.setVisibility(View.VISIBLE);
                    binding.dicrectionButton.setVisibility(View.VISIBLE);
                    state = false;
                } else {
                    binding.autoComplete.setVisibility(View.VISIBLE);
                    binding.subContainer.setVisibility(View.INVISIBLE);
                    binding.dicrectionButton.setVisibility(View.GONE);
                    binding.cancelButton.setVisibility(View.GONE);
                    state = true;
                }

            }
        });

        // xử lý điều hướng tìm phòng -> phòng

        binding.spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < listVert.size(); i++) {
                    if (parent.getSelectedItem().toString().equals(listVert.get(i).getName())) {
                        vEnd = listVert.get(i);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vEnd = null;
                vStart = null;
            }
        });

        binding.spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < listVert.size(); i++) {
                    if (parent.getSelectedItem().toString().equals(listVert.get(i).getName())) {
                        vStart = listVert.get(i);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vEnd = null;
                vStart = null;
            }
        });

    }

    public Vert convertToGlbLat(double x, double y) {
        Vert userX = new Vert();
        double lcLatTemp, lcLongTemp;
        //Converting (x, y) to (latitude, longitude)
        lcLatTemp = lat0 + 0.18 * pow(10, -4) * (x / 100 * cos(0.01745329252) - y / 100 * sin(0.01745329252));
        userX.setLat(lcLatTemp);
        lcLongTemp = long0 + 0.1975 * pow(10, -4) * (x / 100 * cos(0.01745329252) + y / 100 * sin(0.01745329252));
        userX.setLng(lcLongTemp);
        return userX;
    }

    public int findMax(Vert x) {
        Vert vX = new Vert();
        int tempI = 0;
        double min = 100.0;
        double distance, t;
        for (int i = 0; i < listVert.size(); i++) {
            distance = pow(abs(x.getLat() - listVert.get(i).getLat()), 2) + pow(abs(x.getLng() - listVert.get(i).getLng()), 2);
            t = sqrt(distance);
            if (t < min) {
                min = t;
                tempI = i;
            }
        }
        return tempI;
    }

    public void findShortestPath(Vert vTempStart, Vert vTempEnd) {
        PathFinder shortestPath = new PathFinder();
        shortestPath.ShortestP(vTempStart);
        System.out.println("start " + vTempStart.getName());
        List<Double> pathlng = new ArrayList<>();
        List<Double> pathlat = new ArrayList<>();
        pathlng = shortestPath.getShortestPLong(vTempEnd);
        pathlat = shortestPath.getShortestPLat(vTempEnd);
//        System.out.println(pathlat);
//        System.out.println(pathlng);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(new LatLng(vTempStart.getLat(),vTempStart.getLng())));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(vTempEnd.getLat(), vTempEnd.getLng())));

        for (int i = 1; i < pathlng.size(); i++) {
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(pathlat.get(i - 1), pathlng.get(i - 1)),
                            new LatLng(pathlat.get(i), pathlng.get(i)))
                    .width(5)
                    .color(Color.RED));
        }

    }

    private void startBlackIceService() {
        final Intent intent = new Intent(MapsActivity.this, MQTTService.class);
        startService(intent);
    }

    private void stopBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        stopService(intent);
    }

    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
