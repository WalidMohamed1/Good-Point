package com.helloworld.goodpoint.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class Draw extends View {
    protected Paint boundaryPaint;
    protected Paint textPaint;
    private Rect rect;
    private String Text;
    public void init(int i){
          boundaryPaint = new Paint();
          boundaryPaint.setColor(Color.GREEN);
          boundaryPaint.setStrokeWidth(10f);
        //  boundaryPaint.setStyle(Paint.Style.STROKE);

          textPaint = new Paint();
        if(i == 1)
            textPaint.setColor(Color.GREEN);
        else
            textPaint.setColor(Color.RED);
          textPaint.setTextSize(50f);
          textPaint.setStyle(Paint.Style.FILL);

    }

    public Draw(Context context, String Tex) {
        super(context);
      //  rect = rec;
        Text = Tex;



    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(Text,70,70,textPaint);
       // canvas.drawRect(rect.left+150,rect.top+150,rect.right+150,rect.bottom+150,boundaryPaint);
    }
}
