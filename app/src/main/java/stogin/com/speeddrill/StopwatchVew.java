package stogin.com.speeddrill;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
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

        /* Prepare the paint */
        paint = new Paint();
        bounds = new Rect();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        this.setPadding(10,10,10,10);

        text = "0:00:000";
        textSize = determineTextSize();

        mHandler = new Handler();
        start();
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
