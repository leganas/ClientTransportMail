package com.leganas.mobile;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AndreyLS on 18.02.2017.
 */

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, DirectionCallback{
    public GoogleMap googleMap;
    private String serverKey = "AIzaSyBoQ8zuQ4mboa5070I-6AF-mi7Ea3GYjrs";
    private LatLng camera = new LatLng(55.077330, 30.128333);
    private LatLng origin = new LatLng(55.077330, 30.128333);
    private LatLng destination = new LatLng(55.277330, 30.228333);


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    private interface GeocodingCalBack {
        public void geocodingResult(LatLng latLng);
    }

    public void geogoding(final String address, final GeocodingCalBack geocodingCalBack) throws IOException, JSONException {
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = "http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false";//baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
                    System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
                    final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
                    // как правило наиболее подходящий ответ первый и данные о кординатах можно получить по пути
                    // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
                    JSONObject location = response.getJSONArray("results").getJSONObject(0);
                    location = location.getJSONObject("geometry");
                    location = location.getJSONObject("location");
                    final double lng = location.getDouble("lng");// долгота
                    final double lat = location.getDouble("lat");// широта
                    LatLng result = new LatLng(lat,lng);
                    geocodingCalBack.geocodingResult(result);
//                    System.out.println(String.format("%f,%f", lat, lng));// итоговая широта и долгота
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        myThread.start();
        // тоже самое только при помощи AsyncRequest, ахуенная разница :), ну типа так круче говорят
//        AsyncRequest request = new AsyncRequest();
//        request.execute(address);
    }

    class AsyncRequest extends AsyncTask<String, Integer, LatLng> {
        @Override
        protected LatLng doInBackground(String... arg) {
            LatLng result = null;
            try {
                if (arg[0] == null) return null;
                final String url = "http://maps.google.com/maps/api/geocode/json?address="+arg[0]+"&sensor=false";//baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
                System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
                final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
                // как правило наиболее подходящий ответ первый и данные о кординатах можно получить по пути
                // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
                JSONObject location = response.getJSONArray("results").getJSONObject(0);
                location = location.getJSONObject("geometry");
                location = location.getJSONObject("location");
                final double lng = location.getDouble("lng");// долгота
                final double lat = location.getDouble("lat");// широта
                result = new LatLng(lat,lng);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(LatLng s) {
            super.onPostExecute(s);
            System.out.println(s.latitude + " : " + s.longitude);// итоговая широта и долгота
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng vitebsk = new LatLng(55.077330, 30.128333);
        googleMap.addMarker(new MarkerOptions().position(vitebsk).title("Vitebsk"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 10));


        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);

        try {
            geogoding("Витебск", new GeocodingCalBack() {
                @Override
                public void geocodingResult(LatLng latLng) {
                    System.out.println(latLng.latitude + " : " + latLng.longitude);// итоговая широта и долгота
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin));
            googleMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED));

        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}
