package com.example.michael.mapsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Michael on 2015/12/18.
 */
public class MainActivity extends Activity {
    Button b_logo, b_goin,b_line,b_place;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        b_logo = (Button) findViewById(R.id.button_logo);
        b_goin = (Button) findViewById(R.id.button_goin);
        b_line = (Button) findViewById(R.id.button_line);
        b_place = (Button) findViewById(R.id.button_place);
        b_logo.setOnClickListener(buttonListener);
        b_goin.setOnClickListener(buttonListener);
        b_line.setOnClickListener(buttonListener);
        b_place.setOnClickListener(buttonListener);
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
                case R.id.button_place:

                    intent.setClass(MainActivity.this, PlaceActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    };
}