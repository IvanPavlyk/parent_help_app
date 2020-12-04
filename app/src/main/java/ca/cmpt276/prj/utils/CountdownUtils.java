package ca.cmpt276.prj.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ca.cmpt276.prj.ui.TimeoutActivity;

public enum CountdownUtils {
    instance;


    private int interval = 1000;
    private long curTime = 60 * 1000;
    private long duration = 60 * 1000;
    private final int MSG_INTERVAL = 1001;
    private boolean isPause = false;
    private boolean isStart = false;
    private OnCountdownListener mOnCountdownListener;
    private double rate=1.00;

    public void setOnCountdownListener(OnCountdownListener mOnCountdownListener) {
        this.mOnCountdownListener = mOnCountdownListener;
    }


    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }


    public void setDuration(long duration,double rate) {
        this.duration = duration;
        this.rate=rate;
    }

    public boolean isStart() {
        return isStart;
    }


    public void startCountdown() {
        curTime = duration;
        isPause = false;
        isStart = true;
        handler.removeMessages((int) (MSG_INTERVAL*rate));
        handler.sendEmptyMessage(MSG_INTERVAL);
    }


    public void pauseCountdown() {
        isPause = true;
        handler.removeMessages(MSG_INTERVAL);
    }


    public void resumeCountdown() {
        isPause = false;
        handler.sendEmptyMessage(MSG_INTERVAL);
    }


    public void stopCountdown() {
        isPause = false;
        isStart = false;
        handler.removeMessages(MSG_INTERVAL);
    }

    public boolean isPause() {
        return isPause;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_INTERVAL == msg.what) {
                handler.removeMessages(MSG_INTERVAL);

                if (curTime < 1000) {
                    if (mOnCountdownListener != null) {
                        mOnCountdownListener.onFinish();
                    }
                } else {
                    if (mOnCountdownListener != null) {
                        mOnCountdownListener.updateTime(duration, curTime);
                    }
                    curTime = curTime - 1000;
                    handler.sendEmptyMessageDelayed(MSG_INTERVAL, interval);
                }


            }
        }
    };

    public interface OnCountdownListener {
        void updateTime(long duration, long curTime);

        void onFinish();
    }


}
