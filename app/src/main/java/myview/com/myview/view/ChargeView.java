package myview.com.myview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import myview.com.myview.R;


/**
 * 间隔进度配合属性动画
 */
public class ChargeView extends View {
    private String TAG = this.getClass().getName();
    /*画笔*/
    private Paint mPaint;

    /*item 个数*/
    private int mItemCount;
    /*item 宽度*/
    private float mItemWidth;
    /*间隔宽度*/
    private float mSplitWidth;
    /*item 高度*/
    private float mItemHeight;
    /*圆角半径*/
    private float mItemRadios;
    /*未充电的颜色*/
    private int mUnChargeColor;
    /*动画时间*/
    private int mAnimDuration;
    /*进度*/
    private int progress;
    /*(进度平均值)每一条所占的进度　用来判断进度所占的块数*/
    private int averageProgress;
    /*view的宽度和高度*/
    private int mWidth;
    private int mHeight;
    /*边框的线的宽度*/
    private float mStorkeWidth;
    /*渐变颜色设置*/
    private int startColor;
    private int endColor;
    /*渐变色百分比*/
    private float franch;

    public ChargeView(Context context) {
        super(context);
    }

    public ChargeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChargeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChargeView);
        /*item 个数*/
        mItemCount = array.getInt(R.styleable.ChargeView_cv_item_count, 10);
        /*item 宽度*/
        mItemWidth = array.getDimension(R.styleable.ChargeView_cv_item_width, dp2px(6));
        /*间隔宽度 默认就是item的宽度*/
        mSplitWidth = array.getDimension(R.styleable.ChargeView_cv_split_width, mItemWidth);
        /*item 高度*/
        mItemHeight = array.getDimension(R.styleable.ChargeView_cv_item_height, dp2px(16));
        /*圆角半径*/
        mItemRadios = array.getDimension(R.styleable.ChargeView_cv_border_cornor_radius, dp2px(2));
        mStorkeWidth = array.getDimension(R.styleable.ChargeView_cv_storke_width, 1f);
        /*充电进度中的开始颜色 -14909263(色值)*/
        startColor = array.getInt(R.styleable.ChargeView_cv_item_charge_start_color, Color.parseColor("#0E74A7"));
        /*充电完成结束的颜色 -5249793(色值)*/
        endColor = array.getInt(R.styleable.ChargeView_cv_item_charge_end_color, Color.parseColor("#95D8FF"));
        /*未充电的颜色*/
        mUnChargeColor = array.getInt(R.styleable.ChargeView_cv_item_unCharge_color, 0xff524747);
        /*动画时间*/
        mAnimDuration = array.getInt(R.styleable.ChargeView_cv_anim_duration, 3);
        progress = array.getInt(R.styleable.ChargeView_cv_progress, 0);
        array.recycle();
        franch = 1f / mItemCount;
        /*进度均值*/
        averageProgress = 100 / mItemCount;
        initPaint();
    }

    /**
     * 初始化画笔  填充 抗锯齿
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAllUnChargeItem(canvas);
        drawAllChargeItem(canvas);
    }

    /**
     * 绘制所有的默认进度
     *
     * @param canvas
     */
    private void drawAllUnChargeItem(Canvas canvas) {
        mPaint.setColor((mUnChargeColor));
        for (int i = 0; i < mItemCount; i++) {
            RectF rectF = new RectF(i * mItemWidth + i * mSplitWidth,
                    0,
                    (i + 1) * mItemWidth + i * mSplitWidth,
                    mHeight);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStorkeWidth);
            canvas.drawRoundRect(rectF, mItemRadios, mItemRadios, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rectF, mItemRadios, mItemRadios, mPaint);
        }
    }

    /**
     * 绘制已经充好电的块
     *
     * @param canvas
     */
    private void drawAllChargeItem(Canvas canvas) {
        /*当前进度所占进度块个数*/
        int currentProNum = progress / averageProgress;
        for (int i = 0; i < currentProNum; i++) {
            RectF rectF = new RectF(i * mItemWidth + i * mSplitWidth,
                    0,
                    (i + 1) * mItemWidth + i * mSplitWidth,
                    mHeight);
            mPaint.setColor(caculateColor(startColor, endColor, i * franch));
            /*画边框*/
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStorkeWidth);
            canvas.drawRoundRect(rectF, mItemRadios, mItemRadios, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rectF, mItemRadios, mItemRadios, mPaint);
        }
    }

    private ObjectAnimator anim;

    /**
     * 开启属性动画 当在充电状态中的时候开启
     */
    public void setAnimation() {
        anim = ObjectAnimator.ofInt(this, "progress", 0, getProgress() + 1);
        anim.setDuration(mAnimDuration * 1000);
        anim.setInterpolator(new LinearInterpolator());
        if (getProgress() == 100) {

        } else {
            anim.setRepeatCount(ValueAnimator.INFINITE);
        }
        //属性动画监听事件
        /*anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });*/
        anim.start();
    }

    /**
     * 关闭属性动画
     *
     * @param progress 总体进度 因为this.progress会随着属性动画变化所以直接把真是进度传递进来
     */
    public void closeAnimation(int progress) {
        setProgress(progress);
        if (anim != null) {
            anim.cancel();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = (int) (mItemCount * mItemWidth + mSplitWidth * (mItemCount - 1));
        mHeight = (int) mItemHeight;
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    private int getProgress() {
        return this.progress;
    }

    /**
     * dp转化为px`
     *
     * @param dp
     * @return
     */
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 int类型
     * @param endColor   结束颜色 int类型
     * @param franch     franch 百分比0.5
     * @return 返回int格式的color
     */
    public static int caculateColor(int startColor, int endColor, float franch) {
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch));
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor   结束颜色 （格式#FFFFFFFF）
     * @param franch     百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static String caculateColor(String startColor, String endColor, float franch) {

        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(7), 16);

        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);
        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);

        return "#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue);

    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
