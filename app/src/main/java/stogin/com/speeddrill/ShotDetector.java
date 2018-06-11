package stogin.com.speeddrill;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Created by William on 11/9/2017.
 *
 * Based off of the code
 * <a href="https://stackoverflow.com/questions/14181449/android-detect-sound-level">here</a>.
 */

public class ShotDetector {
    private static final String TAG = "SoundMeter";

    private OnSoundUpdateListener mListener;
    private Handler mHandler = new Handler();
    private MediaRecorder mRecorder = null;
    private int updateInterval;

    interface OnSoundUpdateListener {
        void shotDetected();
        void amplitudeUpdate(double current, double max);
    }

    public ShotDetector() {
    }


    public void start(OnSoundUpdateListener listener, int intervalMillis) throws IOException {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            mRecorder.prepare();
            mRecorder.start();

            // Discard first amplitude value (0)
            mRecorder.getMaxAmplitude();
        }

        // Start recurring amplitude polling
        mListener = listener;

        updateInterval = intervalMillis;
        mHandler.postDelayed(updateSoundTask, 100);
    }

    public void stop() {
        mHandler.removeCallbacks(updateSoundTask);
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return -1;

    }

    private double max = 0;
    private Runnable updateSoundTask = new Runnable() {
        @Override
        public void run() {
            double amplitude = getAmplitude();
            mListener.amplitudeUpdate(amplitude, max);

            max = Math.max(max, amplitude);

            if (mListener != null && amplitude >= 20000) {
                mListener.shotDetected();
                // Add half a second for debouncing
                mHandler.postDelayed(updateSoundTask, updateInterval + 500);
            } else {
                mHandler.postDelayed(updateSoundTask, updateInterval);
            }
        }
    };
}