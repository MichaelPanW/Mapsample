package com.example.michael.mapsample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;


/**
 * Created by alex on 2016/2/19.
 */
public class PlaceActivity  extends Activity {
    private static final int REQUEST_PLACE_PICKER = 1;

    TextView mName;
    TextView mAddress;
    ListView mCurrentPlace;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Button pickPlace = (Button) findViewById(R.id.pickPlace);
        pickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = intentBuilder.build(PlaceActivity.this);
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
        mCurrentPlace = (ListView) findViewById(R.id.currentPlace);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(
                this,
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        PendingResult  pendingResult = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
                        pendingResult.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                            @Override
                            public void onResult(PlaceLikelihoodBuffer placeLikelihoods) {
                                ArrayList<String> places = new ArrayList<>();
                                for (PlaceLikelihood placeLikelihood : placeLikelihoods)
                                    places.add(String.format("'%s' \n'%s'\n" +
                                                    "'%s'\n" +
                                                    "'%s'可能性: %g", placeLikelihood.getPlace().getName(),
                                            placeLikelihood.getPlace().getPhoneNumber(),
                                            placeLikelihood.getPlace().getAddress(),
                                            placeLikelihood.getPlace().getLatLng(),
                                            placeLikelihood.getLikelihood()));
                                ArrayAdapter arrayAdapter = new ArrayAdapter<>(PlaceActivity.this, android.R.layout.simple_list_item_1, places);
                                mCurrentPlace.setAdapter(arrayAdapter);
                                placeLikelihoods.release();
                            }
                        });


                    };


                    @Override
                    public void onConnectionSuspended(int i) {

                    };




                },
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }


                })
                .addApi(Places.PLACE_DETECTION_API);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = builder.addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                mName.setText(place.getName());
                mAddress.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Place Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.michael.mapsample/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Place Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.michael.mapsample/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}
