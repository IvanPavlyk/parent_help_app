package ca.cmpt276.prj.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import ca.cmpt276.prj.R;

public class CountDownProgress extends View {

    private static final int DEFAULT_CIRCLE_SOLIDE_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_CIRCLE_STROKE_COLOR = Color.parseColor("#D1D1D1");
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private static final int DEFAULT_CIRCLE_RADIUS = 100;

    private static final int PROGRESS_COLOR = Color.parseColor("#F76E6B");
    private static final int PROGRESS_WIDTH = 5;

    private static final int SMALL_CIRCLE_SOLIDE_COLOR = Color.parseColor("#FFFFFF");
    private static final int SMALL_CIRCLE_STROKE_COLOR = Color.parseColor("#F76E6B");
    private static final int SMALL_CIRCLE_STROKE_WIDTH = 2;
    private static final int SMALL_CIRCLE_RADIUS = 6;

    private static final int TEXT_COLOR = Color.parseColor("#F76E6B");
    private static final int TEXT_SIZE = 40;

    private int defaultCircleSolideColor = DEFAULT_CIRCLE_SOLIDE_COLOR;
    private int defaultCircleStrokeColor = DEFAULT_CIRCLE_STROKE_COLOR;
    private int defaultCircleStrokeWidth = dp2px(DEFAULT_CIRCLE_STROKE_WIDTH);
    private int defaultCircleRadius = dp2px(DEFAULT_CIRCLE_RADIUS);

    private int progressColor = PROGRESS_COLOR;
    private int progressWidth = dp2px(PROGRESS_WIDTH);

    private int smallCircleSolideColor = SMALL_CIRCLE_SOLIDE_COLOR;
    private int smallCircleStrokeColor = SMALL_CIRCLE_STROKE_COLOR;
    private int smallCircleStrokeWidth = dp2px(SMALL_CIRCLE_STROKE_WIDTH);
    private int smallCircleRadius = dp2px(SMALL_CIRCLE_RADIUS);

    private int textColor = TEXT_COLOR;
    private int textSize = sp2px();


    private Paint defaultCirclePaint;
    private Paint progressPaint;
    private Paint smallCirclePaint;
    private Paint smallCircleSolidePaint;


    private float currentAngle;


    private boolean isStart = false;


    public CountDownProgress(Context context) {
        this(context, null);
    }

    public CountDownProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NonConstantResourceId")
    public CountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownProgress);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CountDownProgress_default_circle_solide_color:
                    defaultCircleSolideColor = typedArray.getColor(attr, defaultCircleSolideColor);
                    break;
                case R.styleable.CountDownProgress_default_circle_stroke_color:
                    defaultCircleStrokeColor = typedArray.getColor(attr, defaultCircleStrokeColor);
                    break;
                case R.styleable.CountDownProgress_default_circle_stroke_width:
                    defaultCircleStrokeWidth = (int) typedArray.getDimension(attr, defaultCircleStrokeWidth);
                    break;
                case R.styleable.CountDownProgress_default_circle_radius:
                    defaultCircleRadius = (int) typedArray.getDimension(attr, defaultCircleRadius);
                    break;
                case R.styleable.CountDownProgress_progress_color:
                    progressColor = typedArray.getColor(attr, progressColor);
                    break;
                case R.styleable.CountDownProgress_progress_width:
                    progressWidth = (int) typedArray.getDimension(attr, progressWidth);
                    break;
                case R.styleable.CountDownProgress_small_circle_solide_color:
                    smallCircleSolideColor = typedArray.getColor(attr, smallCircleSolideColor);
                    break;
                case R.styleable.CountDownProgress_small_circle_stroke_color:
                    smallCircleStrokeColor = typedArray.getColor(attr, smallCircleStrokeColor);
                    break;
                case R.styleable.CountDownProgress_small_circle_stroke_width:
                    smallCircleStrokeWidth = (int) typedArray.getDimension(attr, smallCircleStrokeWidth);
                    break;
                case R.styleable.CountDownProgress_small_circle_radius:
                    smallCircleRadius = (int) typedArray.getDimension(attr, smallCircleRadius);
                    break;
                case R.styleable.CountDownProgress_text_color:
                    textColor = typedArray.getColor(attr, textColor);
                    break;
                case R.styleable.CountDownProgress_text_size:
                    textSize = (int) typedArray.getDimension(attr, textSize);
                    break;
            }
        }
        typedArray.recycle();
        setPaint();
    }

    private void setPaint() {
        defaultCirclePaint = new Paint();
        defaultCirclePaint.setAntiAlias(true);
        defaultCirclePaint.setDither(true);
        defaultCirclePaint.setStyle(Paint.Style.STROKE);
        defaultCirclePaint.setStrokeWidth(defaultCircleStrokeWidth);
        defaultCirclePaint.setColor(defaultCircleStrokeColor);
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setDither(true);
        smallCirclePaint.setStyle(Paint.Style.STROKE);
        smallCirclePaint.setStrokeWidth(smallCircleStrokeWidth);
        smallCirclePaint.setColor(smallCircleStrokeColor);

        smallCircleSolidePaint = new Paint();
        smallCircleSolidePaint.setAntiAlias(true);
        smallCircleSolidePaint.setDither(true);
        smallCircleSolidePaint.setStyle(Paint.Style.FILL);
        smallCircleSolidePaint.setColor(smallCircleSolideColor);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize;
        int heightSize;
        int strokeWidth = Math.max(defaultCircleStrokeWidth, progressWidth);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingLeft() + defaultCircleRadius * 2 + strokeWidth + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + defaultCircleRadius * 2 + strokeWidth + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        canvas.drawCircle(defaultCircleRadius, defaultCircleRadius, defaultCircleRadius, defaultCirclePaint);

        float mStartSweepValue = -90;
        canvas.drawArc(new RectF(0, 0, defaultCircleRadius * 2, defaultCircleRadius * 2), mStartSweepValue, 360 * currentAngle, false, progressPaint);


        float extraDistance = 0.7F;
        float currentDegreeFlag = 360 * currentAngle + extraDistance;
        float smallCircleX = 0, smallCircleY = 0;
        float radian = (float) Math.abs(Math.PI * currentDegreeFlag / 180);
        smallCircleX = (float) Math.abs(Math.sin(radian) * defaultCircleRadius + defaultCircleRadius);
        smallCircleY = (float) Math.abs(defaultCircleRadius - Math.cos(radian) * defaultCircleRadius);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleRadius, smallCirclePaint);
        canvas.drawCircle(smallCircleX, smallCircleY, smallCircleRadius - smallCircleStrokeWidth, smallCircleSolidePaint);

        canvas.restore();

    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                CountDownProgress.TEXT_SIZE, getResources().getDisplayMetrics());

    }


    public void setCountdownTime(long countdownTime) {

    }

    public void startCountDownTime() {
        isStart = true;
        setClickable(false);
        currentAngle = 0;
        postInvalidate();
    }

    public void stopCountDownTime() {
        isStart = false;
        setClickable(false);
        currentAngle = 0;
        postInvalidate();
    }


    public void updateProgress(float progress) {
        currentAngle = progress;
        postInvalidate();
    }



}

