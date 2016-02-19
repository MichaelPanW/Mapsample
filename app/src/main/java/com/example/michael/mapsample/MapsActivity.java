package com.example.michael.mapsample;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private AdapterView spinner01;
    Spinner spinner_items;
    private int position;
    // Location請求物件
    private LocationRequest locationRequest;
    private Marker currentMarker, itemMarker;
    // 記錄目前最新的位置
    private GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    private GoogleApiClient googleApiClient;
    private LatLng mysit;
    boolean ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        // 建立Google API用戶端物件
        configGoogleApiClient();

        // 建立Location請求物件
        configLocationRequest();

        // 讀取記事儲存的座標
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lng = intent.getDoubleExtra("lng", 0.0);

        mMap.setOnMarkerClickListener(this);
        // 如果記事已經儲存座標
        if (lat != 0.0 && lng != 0.0) {
            // 建立座標物件
            LatLng itemPlace = new LatLng(lat, lng);
            mysit=itemPlace;
            // 加入地圖標記
            // 移動地圖
            moveMap(itemPlace);
        } else {
            // 連線到Google API用戶端
            if (!googleApiClient.isConnected()) {
                googleApiClient.connect();
            }
        }
        if (mMap != null) {

            // Enable MyLocation Button in the Map
            mMap.setMyLocationEnabled(true);

            // Setting onclick event listener for the map
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {



                }
            });
        }
        spinner_items = (Spinner) findViewById(R.id.spinner01);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_items.setAdapter(adapter);
        spinner_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                MapsActivity.this.position = position;

                setUpMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ch=true;
    }


    // 建立Google API用戶端物件
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // 建立Location請求物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setUpMapIfNeeded() {
// Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
// Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

// Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        switch (position) {
            case 0:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.544465, 120.810724)).title("加油站").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.532378, 120.817162)).title("加油站").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.539561, 120.814606)).title("加油站").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)));
                mMap.addMarker(new MarkerOptions().position(new LatLng( 24.560210, 120.822760)).title("加油站").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)));
                break;
            case 1:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.546396, 120.816277)).title("便利商店").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_711)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.545420, 120.813058)).title("便利商店").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_711)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.541477, 120.819238)).title("便利商店").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_711)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.550261, 120.815419)).title("便利商店").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_711)));
                break;
            case 2:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.543317, 120.816786)).title("醫院").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_hos)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.541256, 120.817996)).title("醫院").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_hos)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.549823, 120.814947)).title("醫院").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_hos)));
                break;

            case 3:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.550129, 120.816156)).title("郵局").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_yo)));


                break;
            case 4:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.550300, 120.815004)).title("停車場").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_p)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(24.545654, 120.812215)).title("停車場").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_p)));
                break;
        }

    }



    private AdapterView.OnItemSelectedListener x1 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MapsActivity.this.position = position;
            Toast toast1 = Toast.makeText(MapsActivity.this, position, Toast.LENGTH_SHORT); //建立物件
            setUpMap();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        if (itemMarker != null) {
                            itemMarker.showInfoWindow();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, MapsActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override

    public void onLocationChanged(Location location) {
        // 位置改變
        // Location參數是目前的位置
        currentLocation = location;
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());
        mysit=latLng;
        // 設定目前位置的標記
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.stu)));
        }
        else {
            currentMarker.setPosition(latLng);
        }

        // 移動地圖到目前的位置

       if(ch){
           moveMap(latLng);
           ch=false;
       }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        return url;
    }

    /**從URL下載JSON資料的方法**/
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** 解析JSON格式 **/
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);  //導航路徑寬度
                lineOptions.color(Color.BLUE); //導航路徑顏色

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }
    @Override
    public boolean onMarkerClick(Marker arg0) {
// TODO Auto-generated method stub

        // Already two locations

            mMap.clear();
            setUpMap();
        mMap.addMarker(new MarkerOptions().position(mysit).icon(BitmapDescriptorFactory.fromResource(R.drawable.stu)));

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(arg0.getPosition());

        /**
         * 起始及終點位置符號顏色
         */



        // Add new marker to the Google Map Android API V2

        // Checks, whether start and end locations are captured

        LatLng origin =mysit;
        LatLng dest = arg0.getPosition();

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions
        // API
        downloadTask.execute(url);
        return false;
    }
}