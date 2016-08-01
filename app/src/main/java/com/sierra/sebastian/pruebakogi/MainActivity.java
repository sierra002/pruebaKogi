package com.sierra.sebastian.pruebakogi;

import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Chronometer crono;
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
        crono = mChronometer;
        String base = "00:00:00:00:000";
        if (mChronometer != null) {
            mChronometer.setText(base);
        }
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.appLayout);
        if (rl != null) {
            rl.setBackgroundColor(Color.RED);
        }
        final TableLayout tl = (TableLayout) findViewById(R.id.tblLayout);
        this.tl = tl;
        /*stop*/
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        if (stopBtn != null) {
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChronometer != null) {
                        mChronometer.stop();
                    }
                    if (rl != null) {
                        rl.setBackgroundColor(Color.RED);
                    }
                    Toast.makeText(getBaseContext(), "Cronometro pausado.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        /*start*/
        Button startBtn = (Button) findViewById(R.id.startBtn);
        if (startBtn != null) {
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChronometer != null) {
                        mChronometer.start();
                    }
                    if (rl != null) {
                        rl.setBackgroundColor(Color.YELLOW);
                    }
                    Toast.makeText(getBaseContext(), "Cronometro iniciado.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        /*reset*/
        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        if (resetBtn != null) {
            resetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChronometer != null) {
                        addElementToTableAtBeggining(tl, (String) mChronometer.getText());
                        mChronometer.reset();
                    }
                    if (rl != null) {
                        rl.setBackgroundColor(Color.RED);
                    }
                    Toast.makeText(getBaseContext(), "Cronometro reiniciado.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addElementToTableAtBeggining(TableLayout tl, String time) {
        if (!time.equals("00:00:00:00:000")) {
            TextView textView = new TextView(this);
            String text = "Tiempo total: " + time;
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            textView.setText(text);
            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < tl.getChildCount(); i++) {
                viewList.add(tl.getChildAt(i));
            }
            tl.removeAllViews();
            tl.addView(textView);
            int count = 0;
            for (View view : viewList) {
                if (count < 3)
                    tl.addView(view);
                count++;
            }
        } else
            Toast.makeText(getBaseContext(), "El cronometro esta vacio.",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("day", crono.getDays());
        savedInstanceState.putInt("hours", crono.getHours());
        savedInstanceState.putInt("minutes", crono.getMinutes());
        savedInstanceState.putInt("seconds", crono.getSeconds());
        savedInstanceState.putInt("miliseconds", crono.getMilliseconds());
        savedInstanceState.putString("text", (String) crono.getText());
        savedInstanceState.putBoolean("state", crono.ismStarted());
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < tl.getChildCount(); i++) {
            stringList.add((String) ((TextView) tl.getChildAt(i)).getText());
        }
        savedInstanceState.putStringArrayList("tblVal", (ArrayList<String>) stringList);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null == crono) {
            System.out.println("si");
        } else {
            crono.setDays(savedInstanceState.getInt("day"));
            crono.setHours(savedInstanceState.getInt("hours"));
            crono.setMinutes(savedInstanceState.getInt("minutes"));
            crono.setSeconds(savedInstanceState.getInt("seconds"));
            crono.setMilliseconds(savedInstanceState.getInt("miliseconds"));
            crono.setText(savedInstanceState.getString("text"));
            if (savedInstanceState.getBoolean("state")) {
                crono.start();
            }
            System.out.println();
            for (String tblVal : savedInstanceState.getStringArrayList("tblVal")) {
                TextView textView = new TextView(this);
                textView.setTextSize(20);
                textView.setTextColor(Color.BLACK);
                textView.setText(tblVal);
                tl.addView(textView);
            }
        }
    }
}
