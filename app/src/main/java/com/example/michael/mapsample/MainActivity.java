package com.example.michael.mapsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by Michael on 2015/12/18.
 */
public class MainActivity extends Activity {
    Button b_logo, b_goin,b_line,b_place,b_geo;
    private static final int REQUEST_PLACE_PICKER = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        b_logo = (Button) findViewById(R.id.button_logo);
        b_goin = (Button) findViewById(R.id.button_goin);
        b_line = (Button) findViewById(R.id.button_line);
        b_place = (Button) findViewById(R.id.button_placeui);
        b_geo = (Button) findViewById(R.id.button_geo);
        b_logo.setOnClickListener(buttonListener);
        b_goin.setOnClickListener(buttonListener);
        b_line.setOnClickListener(buttonListener);
        b_place.setOnClickListener(buttonListener);
        b_geo.setOnClickListener(buttonListener);
    }

    private android.view.View.OnClickListener buttonListener = new Button.OnClickListener() {

        //(android.view.View.OnClickListener可以置換成Button.OnClickListener)
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.button_logo:

                    intent.setClass(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_goin:

                    intent.setClass(MainActivity.this, NavigationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_line:

                    intent.setClass(MainActivity.this, RememberActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_placeui:

                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    try {
                        intent = intentBuilder.build(MainActivity.this);
                        startActivityForResult(intent, REQUEST_PLACE_PICKER);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button_geo:

                    intent.setClass(MainActivity.this, PlaceActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                b_place.setText(place.getName() +" "+ place.getAddress());
            }
        }
    }
}