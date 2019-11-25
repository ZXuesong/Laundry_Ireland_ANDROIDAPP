package com.example.zxs.laundryinireland;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class TodayFragment extends Fragment {

    private TextView time;
    private SeekBar sb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_fragment, container, false);
        time = (TextView) view.findViewById(R.id.today_time);
        sb = (SeekBar) view.findViewById(R.id.seekbar);
        time.setText(sb.getProgress()+"  p.m");
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(sb.getProgress()<12) {
                    time.setText(sb.getProgress() + " a.m");
                }else{
                    time.setText(sb.getProgress() + " p.m");
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

}
