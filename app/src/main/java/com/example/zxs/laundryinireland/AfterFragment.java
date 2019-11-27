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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import static com.example.zxs.laundryinireland.TodayFragment.listFore;

public class AfterFragment extends Fragment {

    private TextView to,se,th;
    private TextView todt,sedt,thdt;
    double toScore,seScore,thScore = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.after_fragment, container, false);
        to = (TextView) view.findViewById(R.id.tomorrow);
        se = (TextView) view.findViewById(R.id.secondday);
        th = (TextView) view.findViewById(R.id.thirdday);
        todt = (TextView)view.findViewById(R.id.tomorrow_dry) ;
        sedt = (TextView)view.findViewById(R.id.secondday_dry) ;
        thdt = (TextView)view.findViewById(R.id.thirdday_dry) ;
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        long timeStampLong = cal.getTimeInMillis()/1000;
        final int timeStamp = (int)timeStampLong ;
        int tomTs = timeStamp+24*3600;
        int secTs = timeStamp+48*3600;
        int thiTs = timeStamp+72*3600;
        String urlTom = getUrl(thiTs);
        String urlSec = getUrl(secTs);
        String urlThi = getUrl(thiTs);
        getScore(urlTom,1);
//        String shortScoreTom = String.format("%.1f",toScore);
//        to.setText("Tomorrow__________"+shortScoreTom+"/4");
        getScore(urlSec,2);
//        String shortScoreSec = String.format("%.1f",seScore);
//        se.setText(getWeek(i+1)+shortScoreSec+"/4");
        getScore(urlThi,3);
//        Log.i("toScore1",""+toScore);
//        String shortScoreThi = String.format("%.1f",thScore);
//        th.setText(getWeek(i+2)+shortScoreThi+"/4");
        return view;
    }

    public static String getWeek(int i) {

        switch (i) {
            case 1:
                return "Monday______________";
            case 2:
                return "Tuesday_____________";
            case 3:
                return "Wednesday___________";
            case 4:
                return "Thursday___________";
            case 5:
                return "Friday______________";
            case 6:
                return "Saturday____________";
            case 7:
                return "Sunday______________";
            default:
                return "";
        }
    }

    private String getUrl(int ts){
        int i = listFore.size();
        int forecastIndex = 0;
        for(int k=0;k<i;k++){
            if(ts<listFore.get(k).dt){
                forecastIndex = k-1;
                break;
            }
        }
        if(forecastIndex<0){
            forecastIndex = i-1;
        }
        TodayFragment.ForecastDetail cur = listFore.get(forecastIndex);
        String url = "http:178.128.46.9/getForecast?pwd=comp47570&temp="+cur.temp+"&hum="+cur.humidity+"&pres="+cur.pressure+"&weat="+cur.weather+"&wind="+cur.wind;
        return url;
    }

    public void getScore(String url,final int i){
        final String[] esDryTime ={"N.D",
                ">4 h",
                "4 h",
                "2 h",
                "1 h"
        };
        HttpUtil.sendRequestWithOkhttp(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("result",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("result","ddasdasdasdas");
                double d = parseJsonWithJsonObjectFlask(response);
                final String shortScore = String.format("%.1f",d);
                final int tdtn = (int)d;
                if(i==1){
                    AfterFragment.this.to.post(new Runnable() {
                        @Override
                        public void run() {
                            to.setText("Tomorrow__________"+shortScore+"/4");
                            todt.setText(esDryTime[tdtn]);
                        }
                    });
                }else if(i==2){
                    AfterFragment.this.se.post(new Runnable() {
                        @Override
                        public void run() {
                            se.setText(getWeek(i+1)+shortScore+"/4");
                            sedt.setText(esDryTime[tdtn]);
                        }
                    });
                }else if(i==3){
                    AfterFragment.this.th.post(new Runnable() {
                        @Override
                        public void run() {
                            th.setText(getWeek(i+2)+shortScore+"/4");
                            thdt.setText(esDryTime[tdtn]);
                        }
                    });
                }
            }
        });
    }

    private double parseJsonWithJsonObjectFlask(Response response)throws IOException{
        String responseData=response.body().string();
        double score=0.0;
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            score = Double.valueOf(jsonObject.get("indoorScore").toString());
            Log.i("toScore",""+score);
//            if(i == 1){
//                toScore = Double.valueOf(jsonObject.get("indoorScore").toString());
//                Log.i("toScore2",""+toScore);
//            }else if(i == 2){
//                seScore = Double.valueOf(jsonObject.get("indoorScore").toString());
//                Log.i("seScore",""+Double.valueOf(jsonObject.get("indoorScore").toString()));
//            }else if(i==3){
//                thScore = Double.valueOf(jsonObject.get("indoorScore").toString());
//                Log.i("thScore",""+Double.valueOf(jsonObject.get("indoorScore").toString()));
//            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return score;
    }
}
