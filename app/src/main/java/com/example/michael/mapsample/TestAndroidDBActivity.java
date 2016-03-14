package com.example.michael.mapsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class TestAndroidDBActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dblink);

        findViews();
        setListeners();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    private Button button_get_record;
    private Button button_Insert;
    private EditText Edit_value;


    private void findViews() {
        button_get_record = (Button)findViewById(R.id.get_record);
        button_Insert = (Button)findViewById(R.id.send_btn);
    }

    private void setListeners() {
        button_get_record.setOnClickListener(getDBRecord);
        button_Insert.setOnClickListener(insertDB);

    }

    private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TableLayout user_list = (TableLayout)findViewById(R.id.user_list);
            user_list.setStretchAllColumns(true);
            TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            try {
                String result = DBConnector.ReJeson("SELECT * FROM weblog");

                /*
                    SQL 結果有多筆資料時使用JSONArray
                    只有一筆資料時直接建立JSONObject物件
                    JSONObject jsonData = new JSONObject(result);
                */
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    TableRow tr = new TableRow(TestAndroidDBActivity.this);
                    tr.setLayoutParams(row_layout);
                    tr.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView user_acc = new TextView(TestAndroidDBActivity.this);
                    user_acc.setText(jsonData.getString("log_id"));

                    user_acc.setLayoutParams(view_layout);

                    TextView user_pwd = new TextView(TestAndroidDBActivity.this);
                    user_pwd.setText(jsonData.getString("data"));
                    user_pwd.setLayoutParams(view_layout);

                    tr.addView(user_acc);
                    tr.addView(user_pwd);
                    user_list.addView(tr);

                }
            } catch(Exception e) {
                // Log.e("log_tag", e.toString());
            }
        }
    };
    private Button.OnClickListener insertDB = new Button.OnClickListener() {
        public void onClick(View v) {
            Edit_value=(EditText)findViewById(R.id.txt_message);
            String Data=Edit_value.getText().toString();
            try {
                Data=DBConnector.executeQuery(Data);
                Toast.makeText(TestAndroidDBActivity.this, Data, Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
    };
}