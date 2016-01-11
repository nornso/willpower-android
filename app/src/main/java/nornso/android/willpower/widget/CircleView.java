package nornso.android.willpower.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import nornso.android.willpower.R;

/**
 * Created by Wu on 2015/12/31.
 */
public class CircleView extends View {
    private int mCircleColor;
    private Paint mCirclePaint;


    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCirclePaint = new Paint();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleView, 0, 0);

        try {
            mCircleColor = a.getInteger(R.styleable.CircleView_viewColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int widthHalf = this.getMeasuredWidth() / 2;
        int heighHalf = this.getMeasuredHeight() / 2;

        int radius = widthHalf > heighHalf ? heighHalf - 5 : widthHalf - 5;
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setAntiAlias(true);

        canvas.drawCircle(widthHalf, heighHalf, radius, mCirclePaint);
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int color) {
        mCircleColor = color;
        invalidate();
        requestLayout();
    }


}
