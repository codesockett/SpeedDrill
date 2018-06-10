package stogin.com.speeddrill;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class StopwatchVew extends View {

    private Paint paint;
    private String text;
    private Rect bounds;
    private float verticalPadding = 10;
    private float horizontalPadding = 10;

    public StopwatchVew(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        /* Prepare the paint */
        paint = new Paint();
        bounds = new Rect();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        this.setPadding(10,10,100,10);

        text = "0:00:00";
    }

    @Override
    protected void onDraw(Canvas canvas) {



        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float fullWidthSize = testTextSize * (this.getMeasuredWidth() - this.getPaddingRight() - this.getPaddingLeft()) / bounds.width();
        float fullHeightSize = testTextSize * (this.getMeasuredHeight() - this.getPaddingBottom() - this.getPaddingTop()) / bounds.height();

        paint.setTextSize(Math.min(fullHeightSize, fullWidthSize));

        canvas.drawText(text, this.getPaddingLeft(), this.getMeasuredHeight() - getPaddingBottom(), paint);
    }

    public void start() {

    }

    public void pause() {

    }
}
