package com.example.zxs.laundryinireland;

import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TodayFragment extends Fragment {

    private TextView time;
    private TextView tScore;
    private TextView tdt;
    private SeekBar sb;
    private static final String url = "http://api.openweathermap.org/data/2.5/forecast?q=Dublin,ie&APPID=1f46aa0715e3f8c52ea3d1fd985ccf8a";
    String zxs ="zxs";
    public static List<ForecastDetail> listFore = new ArrayList<ForecastDetail>();
    double timeScore = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_fragment, container, false);
        getForecast();
        time = (TextView) view.findViewById(R.id.today_time);
        sb = (SeekBar) view.findViewById(R.id.seekbar);
        tScore = (TextView) view.findViewById(R.id.today_number) ;
        tdt = (TextView) view.findViewById(R.id.today_dry_time) ;
        final String[] esDryTime ={"N.D",
                ">4 h",
                "4 h",
                "2 h",
                "1 h"
        };
        Calendar cal = Calendar.getInstance();
        long timeStampLong = cal.getTimeInMillis()/1000;
        final int timeStamp = (int)timeStampLong ;
        Log.i("future",""+timeStamp);
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        sb.setMax(24-hour);
        sb.setProgress(0);
        if(hour<12){
            time.setText(hour+"  a.m");
        }else{
            time.setText(hour+"  p.m");
        }
        DatabaseReference mDatabase ;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference[] weather = {mDatabase.child("devices").child("80:7D:3A:3C:D2:44").child("records")};
        Query last = weather[0].orderByKey().limitToLast(1);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    double rec = Double.parseDouble(ds.child("OutdoorDryScore").getValue().toString());
                    String score = String.format("%.1f",rec);
                    int tdtn = (int)rec;
                    tScore.setText(score+"/4");
                    tdt.setText("estimated drying time: "+esDryTime[tdtn]);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        last.addValueEventListener(eventListener);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int current = sb.getProgress()+hour;
                if(current<12) {
                    time.setText(current+ " a.m");
                }else{
                    time.setText( current+ " p.m");
                }
                if(current-hour==0){
                    DatabaseReference mDatabase ;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference[] weather = {mDatabase.child("devices").child("80:7D:3A:3C:D2:44").child("records")};
                    Query last = weather[0].orderByKey().limitToLast(1);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                double rec = Double.parseDouble(ds.child("OutdoorDryScore").getValue().toString());
                                String score = String.format("%.1f",rec);
                                int tdtn = (int)rec;
                                tScore.setText(score+"/4");
                                tdt.setText("estimated drying time: "+esDryTime[tdtn]);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    last.addValueEventListener(eventListener);
                }else{
                    int future = timeStamp+3600*(current-hour);
                    int i = listFore.size();
                    int forecastIndex = 0;
                    for(int k=0;k<i;k++){
                        if(future<listFore.get(k).dt){
                            forecastIndex = k-1;
                            break;
                        }
                    }
                    if(forecastIndex<0){
                        forecastIndex = i-1;
                    }
                    ForecastDetail cur = listFore.get(forecastIndex);
                    String url = "http:178.128.46.9/getForecast?pwd=******&temp="+cur.temp+"&hum="+cur.humidity+"&pres="+cur.pressure+"&weat="+cur.weather+"&wind="+cur.wind;
                    getScore(url);
                    String shortScore = String.format("%.1f",timeScore);
                    int tdtn = 0;
                    if(timeScore!=0){
                        tdtn = (int) timeScore;
                    }
                    tScore.setText(shortScore + "/4");
                    tdt.setText("estimated drying time: "+esDryTime[tdtn]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public void getScore(String url){
        HttpUtil.sendRequestWithOkhttp(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("result",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("result","ddasdasdasdas");
                parseJsonWithJsonObjectFlask(response);
            }
        });
    }

    public void getForecast(){
        HttpUtil.sendRequestWithOkhttp(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("result",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("result","cxzcxcxczxczx");
                parseJsonWithJsonObject(response);
            }
        });
    }

    private void parseJsonWithJsonObjectFlask(Response response)throws IOException{
        String responseData=response.body().string();
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            timeScore = Double.valueOf(jsonObject.get("indoorScore").toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void parseJsonWithJsonObject(Response response) throws IOException {
        String responseData=response.body().string();
        List<Map<String, String>> list = null;

        try{
            JSONArray jsonArray = new JSONObject(responseData).getJSONArray("list");
            JSONObject subJsonObject;
            list = new ArrayList<Map<String, String>>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                subJsonObject = jsonArray.getJSONObject(i);

                list.add(getMap(subJsonObject.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       for( int i = 0; i < list.size(); i++ ) {
           int dt = Integer.parseInt(list.get(i).get("dt"));
           String weahter = list.get(i).get("weather").substring(19).substring(0,list.get(i).get("weather").substring(19).indexOf(",")-1);
           double wind = Double.parseDouble(list.get(i).get("wind").substring(9).substring(0,list.get(i).get("wind").substring(9).indexOf(",")));
           double temp = Double.parseDouble(list.get(i).get("main").substring(8).substring(0,list.get(i).get("main").substring(8).indexOf(",")))-273.15;
           int pressure = Integer.parseInt(list.get(i).get("main").substring(list.get(i).get("main").indexOf("pressure")+10).substring(0,list.get(i).get("main").substring(list.get(i).get("main").indexOf("pressure")+10).indexOf(",")));
           int hum = Integer.parseInt(list.get(i).get("main").substring(list.get(i).get("main").indexOf("humidity")+10).substring(0,list.get(i).get("main").substring(list.get(i).get("main").indexOf("humidity")+10).indexOf(",")));
           ForecastDetail fore = new ForecastDetail(dt,temp,pressure,hum,weahter,wind);
           listFore.add(i,fore);
           Log.i("dtdtdtdtdtdtdtdt",""+fore.dt);
       }
    }

    public static Map<String, String> getMap(String jsonString)

    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
        Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext())
            {
                key = (String) keyIter.next();
                value = jsonObject.get(key).toString();
                valueMap.put(key, value);
            }
            return valueMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;

    }

    public class ForecastDetail{
        public int dt;
        public double temp;
        public int pressure;
        public int humidity;
        public String weather;
        public double wind;

        public ForecastDetail(int d,double t,int p,int h,String we,double wi){
            this.dt = d;
            this.temp = t;
            this.pressure = p;
            this.humidity = h;
            this.weather = we;
            this.wind = wi;
        }
    }

}
