package com.tara.attendanceforstudent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RectOverlay extends GraphicOverlay.Graphic {

    int Rect_color = Color.RED;
    float stroke_width = 4.0f;
    Paint rectPaint;
    GraphicOverlay graphicOverlay;
    Rect rect;

    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;

        rectPaint = new Paint();
        rectPaint.setColor(Rect_color);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(stroke_width);
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateY(rectF.top);
        rectF.bottom = translateY(rectF.bottom);
        canvas.drawRect(rectF,rectPaint);
    }
}
