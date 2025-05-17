package com.example.lab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class BubbleLevelView extends View {
    private Paint circlePaint;
    private Paint bubblePaint;
    private Paint overlayPaint;
    private Paint centerLinePaint;
    private float bubbleX, bubbleY;
    private float centerX, centerY;
    private float radius;
    private float scaleFactor = 1.4f;


    public BubbleLevelView(Context context) {
        super(context);
        init();
    }

    public BubbleLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.purple));
        circlePaint.setStyle(Style.STROKE);
        circlePaint.setStrokeWidth(8);
        circlePaint.setAntiAlias(true);

        bubblePaint = new Paint();
        bubblePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        bubblePaint.setStyle(Style.FILL);
        bubblePaint.setAntiAlias(true);

        overlayPaint = new Paint();
        overlayPaint.setColor(Color.argb(102, 189, 152, 255));
        overlayPaint.setStyle(Style.FILL);
        overlayPaint.setAntiAlias(true);

        centerLinePaint = new Paint();
        centerLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        centerLinePaint.setStrokeWidth(4);
        centerLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 3f * scaleFactor;
        bubbleX = centerX;
        bubbleY = centerY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float plusSize = 30;

        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.drawCircle(centerX, centerY, 40 * scaleFactor, circlePaint);

        canvas.drawLine(centerX - plusSize, centerY, centerX + plusSize, centerY, centerLinePaint);
        canvas.drawLine(centerX, centerY - plusSize, centerX, centerY + plusSize, centerLinePaint);

        canvas.drawCircle(bubbleX, bubbleY, radius, overlayPaint);

        canvas.drawCircle(bubbleX, bubbleY, 40 * scaleFactor, bubblePaint);
    }

    public void updateBubble(float x, float y) {
        float maxOffset = radius;
        float targetX = centerX - x * maxOffset / 9.8f;
        float targetY = centerY + y * maxOffset / 9.8f;

        float distance = (float)Math.sqrt(Math.pow(targetX - centerX, 2) + Math.pow(targetY - centerY, 2));
        if (distance > maxOffset) {
            float ratio = maxOffset / distance;
            targetX = centerX + (targetX - centerX) * ratio;
            targetY = centerY + (targetY - centerY) * ratio;
        }

        bubbleX += (targetX - bubbleX) * 0.1f;
        bubbleY += (targetY - bubbleY) * 0.1f;

        invalidate();
    }
}
