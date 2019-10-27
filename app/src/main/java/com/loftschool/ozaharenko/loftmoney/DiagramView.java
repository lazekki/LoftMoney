package com.loftschool.ozaharenko.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DiagramView extends View {

    private float mExpenses;
    private float mIncome;

    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagramView(Context context) {
        super(context);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void update(float expenses, float income) {
        mExpenses = expenses;
        mIncome = income;
        invalidate();   //call for redraw of the view
    }

    private void init() {
        expensePaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_sky_blue));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        float total = mExpenses + mIncome;

        float expenseAngle = 360f*mExpenses / total;
        float incomeAngle = 360f*mIncome / total;

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(
                xMargin - space,
                yMargin,
                getWidth() - xMargin - space,
                getHeight() - yMargin,
                180 - expenseAngle/2,
                expenseAngle,
                true,
                expensePaint);

        canvas.drawArc(
                xMargin + space,
                yMargin,
                getWidth() - xMargin + space,
                getHeight() - yMargin,
                360 - incomeAngle/2,
                incomeAngle,
                true,
                incomePaint);
    }
}
