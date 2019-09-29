package myview.com.myview.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import myview.com.myview.R;

/**
 * Crate by E470PD on 2019/8/22
 */
public class RoundProgress extends View {
    private float mWidth;
    private float mHeight;
    //进度
    private int cProgress;
    //最大进度
    private int cMaxProgress;
    //圆环的颜色
    private int cRoundColor;
    //圆环进度的颜色
    private int cProgressColor;
    //文字颜色
    private int cTextColor;
    //文字大小
    private float cTextSize;
    //底色进度条宽度
    private float cRoundWidth;
    //半径
    private float cRadius;
    //画笔末端样式
    private int cPathStrokeCap;
    //是否显示中间文字
    private boolean cIsShowText;
    //开始的角度-90 正上方 0 右侧中间位置 90 正下方 180 左侧中间位置 默认-90
    private int cAngle;
    //圆心
    private float center;
    //画笔
    Paint paint;
    //圆环区域
    RectF rectF;
    public final int BUTT = 0;
    public final int ROUND = 1;
    public final int SQUARE = 2;

    public RoundProgress(Context context) {
        super(context);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Round_Progress);
        cProgress = array.getInt(R.styleable.Round_Progress_c_progress, 0);
        cMaxProgress = array.getInt(R.styleable.Round_Progress_c_max_progress, 100);
        cRoundColor = array.getInt(R.styleable.Round_Progress_c_round_color, 0xff524747);
        cProgressColor = array.getInt(R.styleable.Round_Progress_c_progress_color, 0xffE1FFFF);
        cTextColor = array.getInt(R.styleable.Round_Progress_c_text_color, 0xffE1FFFF);
        cTextSize = array.getDimension(R.styleable.Round_Progress_c_text_size, sp2px(getResources(), 18));
        cRoundWidth = array.getDimension(R.styleable.Round_Progress_c_round_stroke_width, dp2px(5));
        cPathStrokeCap = array.getInt(R.styleable.Round_Progress_c_path_stroke_cap, 0);
        cIsShowText = array.getBoolean(R.styleable.Round_Progress_c_is_show_text, false);
        cAngle=array.getInt(R.styleable.Round_Progress_c_angle,-90);
        array.recycle();
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    /**
     * 绘制无进度时 圆环进度条
     *
     * @param paint      画笔
     * @param canvas     画布
     * @param cx         圆心x轴坐标
     * @param cy         圆心y轴坐标
     * @param roundColor 圆的颜色
     * @param roundWidth 画笔宽度
     */
    private void drawableCircle(Paint paint, Canvas canvas, float cx, float cy, int roundColor, float roundWidth) {
        paint.setColor(roundColor);
        paint.setStrokeWidth(roundWidth);
        canvas.drawCircle(cx, cy, cRadius, paint);
    }

    /**
     * 绘制有进度时的进度条
     *
     * @param paint         画笔
     * @param canvas        画布
     * @param progressColor 圆的颜色
     * @param progressWidth 画笔宽度
     */
    private void drawableArc(RectF rectF, Paint paint, Canvas canvas, int progressColor, float progressWidth, int progress) {
        paint.setColor(progressColor);
        paint.setStrokeWidth(progressWidth);
        switch (cPathStrokeCap) {
            case BUTT:
                paint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case ROUND:
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case SQUARE:
                paint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            default:
                paint.setStrokeCap(Paint.Cap.BUTT);
                break;
        }
        canvas.drawArc(rectF, cAngle, 360 * progress / cMaxProgress, false, paint);
    }

    /**
     * 公有文字距离Y轴中心点向下偏移属性
     * 用于绘制文字时 baseLineY 基准线的位置向下的偏移量 以文字高度为基础
     * 下面是是计算方式以及中心点注释
     * {@link #drawText(RectF, String, Canvas)}
     * int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2 + (mItemHeight / publicextPosition)); 中心点向下便宜
     * (int) (rectF.centerY() - top / 2 - bottom / 2）中心点Y轴 控件的中心点 也是将要绘制的文字中心点
     * (mItemHeight / {@link #publicTextPosition}) 既是向下偏移量
     * 默认为2  也就是二分之一 的高度 可以根据显示效果自己调整
     */
    private float publicTextPosition = 2;
    //设置文字所占块的比例 默认0.5
    private float textSizeProportion = 0.5f;

    /**
     * 绘制文字
     *
     * @param rectF
     * @param msg
     * @param canvas
     */
    private void drawText(RectF rectF, String msg, Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(cTextColor);
        textPaint.setTextSize(cTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);//抗锯齿
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
//        RectF rectF = new RectF(center - cRadius, center - cRadius, center + cRadius, center + cRadius);
        //文字基准是基于文字左下角靠近的一个位置 为了向下移动一点 所以这里根据rectF的高度动态设置一下
        int baseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2  /*+(mItemHeight / publicTextPosition)*/);//基线中间点的y轴计算公式
        canvas.drawText(msg, rectF.centerX(), baseLineY, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF = new RectF(center - cRadius, center - cRadius, center + cRadius, center + cRadius);
        drawableCircle(paint, canvas, center, center, cRoundColor, cRoundWidth);
        drawableArc(rectF, paint, canvas, cProgressColor, cRoundWidth, getProgress());
        if (cIsShowText) {
            drawText(rectF, getProgress() + "%", canvas);
        }
    }

    private int getProgress() {
        return this.cProgress;
    }

    public void setProgress(int progress) {
        this.cProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        center = width / 2.0f;
        //半径 控件宽度/2.0f-画笔宽度/2.0f
        cRadius = width / 2.0f - cRoundWidth / 2.0f;
    }

    /**
     * sp 转px
     *
     * @param resources
     * @param sp
     * @return
     */
    public static float sp2px(Resources resources, float sp) {
        float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * dp转化为px`
     *
     * @param dp
     * @return
     */
    protected float dp2px(int dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

}
