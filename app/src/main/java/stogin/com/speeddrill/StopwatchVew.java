package stogin.com.speeddrill;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class StopwatchVew extends View {

    private Paint paint;
    private String text;
    private Rect bounds;
    private Handler mHandler;
    long startTime;
    float textSize;

    public StopwatchVew(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setSaveEnabled(true);
        setSaveFromParentEnabled(true);

        /* Prepare the paint */
        paint = new Paint();
        bounds = new Rect();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        this.setPadding(10,10,10,10);

        text = "0:00:000";
        textSize = determineTextSize();

        mHandler = new Handler();
    }

    private float determineTextSize() {
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float fullWidthSize = testTextSize * (this.getMeasuredWidth() - this.getPaddingRight() - this.getPaddingLeft()) / bounds.width();
        float fullHeightSize = testTextSize * (this.getMeasuredHeight() - this.getPaddingBottom() - this.getPaddingTop()) / bounds.height();
        return Math.min(fullHeightSize, fullWidthSize);
    }


    /* ************** DRAWING ******** */
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        textSize = determineTextSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextSize(textSize);

        canvas.drawText(text, this.getPaddingLeft(), this.getMeasuredHeight() - getPaddingBottom(), paint);
    }

    /* ************************* STATE SAVING AND UPDATING **************** */
    private final String SAVE_KEY_SUPER = "superState";
    private final String SAVE_KEY_START = "startTimeMillis";
    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVE_KEY_SUPER, super.onSaveInstanceState());
        bundle.putLong(SAVE_KEY_START, this.startTime); // ... save stuff
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            this.startTime = bundle.getLong(SAVE_KEY_START); // ... load stuff
            state = bundle.getParcelable(SAVE_KEY_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    /* ************************ PUBLIC INTERRACTION ************************* */
    public void start() {
        startTime = SystemClock.uptimeMillis();
        mHandler.postDelayed(update, 0);
    }

    public void pause() {

    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            long currentTime = SystemClock.uptimeMillis();
            long diff = currentTime - startTime;

            String minutes = String.format("%01d", (int) diff / 60000);
            diff = diff - 60000*((int) diff/60000);
            String seconds = String.format("%02d", (int) (diff / 1000));
            String millis = String.format("%03d",(int) (diff % 1000));
            text = minutes + ":" + seconds + ":" + millis;
            StopwatchVew.this.invalidate();
            mHandler.postDelayed(update, 50);
        }
    };
}
