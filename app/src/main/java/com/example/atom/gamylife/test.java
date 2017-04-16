package com.example.atom.gamylife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Atom on 16/04/2017.
 */

public class test extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmp);

        textView = (TextView) findViewById(R.id.TESTtextView);

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            textView.setText(calendar.getTime().toString());
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.
        }

        @Override
        public void onDateTimeCancel()
        {
            textView.setText("nothing");
            // Overriding onDateTimeCancel() is optional.
        }
    };
}
