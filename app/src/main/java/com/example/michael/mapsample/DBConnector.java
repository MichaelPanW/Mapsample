package com.example.michael.mapsample;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class DBConnector {
    private static String myhtml = "http://ielder.im.nuu.edu.tw/TT/";






    public static String ReJeson(String query_string) {//搜尋資料庫 回傳jeson
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(myhtml+"json.php");//php檔案在這裡
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", query_string));
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //view_account.setText(httpResponse.getStatusLine().toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }

        return result;
        /*  讀取jeson的用法
            String result = DBConnector.ReJeson("SELECT * FROM weblog");
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                jsonData("資料欄位名");
            */
    }
    public static boolean executeQuery(String query_string) {//新增或刪除資料庫 回傳true
        boolean result = false;
        String data=query_string.substring(0,3)+"michael"+query_string.substring(3,query_string.length());
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(myhtml+"excute.php");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", data));
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            String builder = new String();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder+=line;
            }
            inputStream.close();
            if(builder.equals("true"))result=true;
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }

        return result;
    }
}
