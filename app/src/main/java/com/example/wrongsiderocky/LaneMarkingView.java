package com.example.wrongsiderocky;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LaneMarkingView extends View {

    private Paint paint;
    private float laneWidth = 100; // Adjust lane width as needed
    private float spacing = 20; // Adjust spacing between lanes
    private float speed = 5; // Adjust speed as needed
    private float offset = 0; // Initial offset for animation

    public LaneMarkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#FFA500")); // Orange color
        paint.setStrokeWidth(5); // Adjust line width as needed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw multiple rectangular lanes
        float centerY = getHeight() / 2; // Center of the view
        float startX = -laneWidth + offset;
        while (startX < getWidth()) {
            canvas.drawRect(startX, centerY - 20, startX + laneWidth, centerY + 20, paint);
            startX += laneWidth + spacing;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                // Update the offset based on speed for animation
                offset += speed;
                if (offset > laneWidth + spacing) {
                    offset -= laneWidth + spacing; // Reset offset for continuous animation
                }
                invalidate(); // Redraw the view
                postDelayed(this, 16); // Repeat with 60 fps (approximately)
            }
        });
    }
}
