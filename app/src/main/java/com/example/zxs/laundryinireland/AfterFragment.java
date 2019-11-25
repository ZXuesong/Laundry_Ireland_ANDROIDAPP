package com.example.zxs.laundryinireland;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AfterFragment extends Fragment {

    private TextView to,se,th;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.after_fragment, container, false);
        to = (TextView) view.findViewById(R.id.tomorrow);
        se = (TextView) view.findViewById(R.id.secondday);
        th = (TextView) view.findViewById(R.id.thirdday);
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        to.setText("Tomorrow__________0/0");
        se.setText(getWeek(i)+"0/0");
        th.setText(getWeek(i+1)+"0/0");
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
}
