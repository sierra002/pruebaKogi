package com.sierra.sebastian.pruebakogi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by sebastian on 31/07/2016.
 * PruebaKogi
 */
public class Chronometer extends TextView {
    @SuppressWarnings("unused")
    private static final String TAG = "Chronometer";

    public interface OnChronometerTickListener {

        void onChronometerTick(Chronometer chronometer);
    }

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private int days = 0;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    public boolean ismStarted() {
        return mStarted;
    }

    public void setmStarted(boolean mStarted) {
        this.mStarted = mStarted;
    }

    private int milliseconds = 0;
    private OnChronometerTickListener mOnChronometerTickListener;

    private static final int TICK_WHAT = 2;

    private long timeElapsed;

    public Chronometer(Context context) {
        this(context, null, 0);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    public void start() {
        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        updateRunning();
    }

    public void reset() {
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.milliseconds = 0;
        stop();
        String initialValue = "00:00:00:00:000";
        setText(initialValue);
    }

    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {
        System.out.println(now);
        this.timeElapsed = now - mBase;
        mBase = now;
        if (this.timeElapsed < 500) {
            this.milliseconds += this.timeElapsed;
            DecimalFormat df = new DecimalFormat("00");
            DecimalFormat dfMils = new DecimalFormat("000");
            if (this.milliseconds > 1000) {
                this.seconds++;
                this.milliseconds = this.milliseconds - 1000;
                if (this.seconds >= 60) {
                    this.minutes++;
                    this.seconds = this.seconds - 60;
                    if (this.minutes == 60) {
                        this.hours++;
                        this.minutes = 0;
                        if (this.hours == 24) {
                            this.days++;
                            this.hours = 0;
                        }
                    }
                }
            }
            String text = "";
            text += df.format(days) + ":";
            text += df.format(hours) + ":";
            text += df.format(minutes) + ":";
            text += df.format(seconds) + ":";
            text += dfMils.format(milliseconds);
            setText(text);
        }

    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT),
                        5);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }


    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

}

